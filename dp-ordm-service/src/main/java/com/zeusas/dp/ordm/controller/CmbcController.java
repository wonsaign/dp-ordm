package com.zeusas.dp.ordm.controller;

import java.io.IOException;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import org.apache.commons.codec.binary.Base64;
import com.zeusas.common.utils.DSResponse;
import com.zeusas.common.utils.Status;
import com.zeusas.core.utils.StringUtil;
import com.zeusas.dp.ordm.entity.CmbcPayRecord;
import com.zeusas.dp.ordm.entity.Order;
import com.zeusas.dp.ordm.service.CmbcPayRecordService;
import com.zeusas.dp.ordm.service.OrderService;
import com.zeusas.epay.cmbc.utils.CmbcConfig;
import com.zeusas.epay.cmbc.utils.PackUtil;

import cfca.sadk.cmbc.tools.CMBCDecryptKit;
import cfca.sadk.cmbc.tools.DecryptKitException;

@Controller
@RequestMapping("/cmbc")
public class CmbcController {
	

	//设置单独的日志付款日志
	static Logger logger = LoggerFactory.getLogger(CmbcController.class);

	@Autowired
	private OrderService orderService;
	@Autowired
	private CmbcPayRecordService cmbcPayRecordService;
	
	
	@RequestMapping(value = "/index" ,method=RequestMethod.GET)
	public String index(@RequestParam(name="orderID",required=true)Long order_id,ModelMap mp){
		try{
			Order order = orderService.get(order_id);
			if(order==null){
				return "redirect:/ordm/index.do";
			}
			// 下述参数任意为空，订单无效，返回到首页。
			mp.addAttribute("DSResponse",new DSResponse(order));
		}catch (Exception e) {
			logger.error("跳转到民生付失败",order_id,e);
		}
		return "cmbc/cmbc_index";
	}
	/**
	 * 
	 * @param order_id 单号
	 * @param txAmt 金额
	 * @param channel 支付通道0：PC 2：WAP
	 * @param remark 备注
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/confirm" ,method=RequestMethod.POST)
	public @ResponseBody DSResponse cmbcConfirm(@RequestParam(name="orderNo",required=false)String order_no,
			@RequestParam(name="txAmt",required=false)String txAmt,
			@RequestParam(name="channel",required=false)String channel,
			@RequestParam(name="remark",required=false)String remark,
			HttpServletRequest request) throws IOException{
		CMBCDecryptKit userKit = new CMBCDecryptKit();
		try{
			Order order = orderService.getsingleOrder(order_no);
			if(order==null){
				return new DSResponse(Status.FAILURE,"订单为空");
			}
			String orderNo = order.getOrderNo();//订单号
			Double total_fee = order.getPayable();//订单金额
			//下述参数任意为空，订单无效，返回到首页。
			if (StringUtil.isEmpty(orderNo)
					|| StringUtil.isEmpty(total_fee)) {
				return new DSResponse(Status.FAILURE,"无单号和总额");
			}
			request.setCharacterEncoding("utf-8");
			PackUtil packUtil = new PackUtil();
			userKit.Initialize(CmbcConfig.getProperties("cmbc_key"), CmbcConfig.getProperties("cmbc_password"),
					CmbcConfig.getProperties("cmbc_veriSign"));
			// 手机APP支付通道
			System.out.println("channel--"+channel);
			String orderSign = packUtil.packReqBean(orderNo, txAmt, channel, remark);
			String orderData = userKit.SignAndEncryptMessage(orderSign);
			return new DSResponse(orderData);
		}catch(Exception e){
			logger.error("民生付支付失败！ orderID:{}",order_no,e);
		}finally {
			// 密钥释放
			try {
				userKit.Uninitialize();
			} catch (DecryptKitException e) {
				logger.error("密钥反初始化失败",e);
			}
		}
		return new DSResponse(Status.FAILURE,"出现异常");
	}
	
	/**
	 * 接收返回参数
	 * @param request
	 * @throws IOException
	 */
	@RequestMapping(value = "/notifypay",method=RequestMethod.GET)
	public void getNotify(HttpServletRequest request) throws IOException {
		request.setCharacterEncoding("utf-8");

		String orderData = request.getParameter("payresult");

		CMBCDecryptKit userKit = new CMBCDecryptKit();
		try {
			userKit.Initialize(CmbcConfig.getProperties("cmbc_key"), CmbcConfig.getProperties("cmbc_password"),
					CmbcConfig.getProperties("cmbc_veriSign"));

			String order = userKit.DecryptAndVerifyMessage(orderData);
			Assert.notNull(order,"返回密文为空");
			// 使用BASE64来解密
			String result = new String(new Base64().decode(order));
			System.out.println(result);
			// 2017070600001|1|01347|0.16|20170706|095634|0|null| 将结果进行差分比对
			String[] ss = result.split("\\|",-1);
			CmbcPayRecord cpr = new CmbcPayRecord();
			cpr.setOrderNo(ss[0]);
			cpr.setBankFlag(ss[1]);// 行内外标识 0:行内   	1:跨行
			cpr.setTxtAmt(ss[3]);// 金额
			cpr.setDateTime(ss[4]+ss[5]);// 交易日期和时间
			cpr.setBillStatus(ss[6]);// 交易状态 0:success
			cpr.setRemark(ss[8]);// 备注 
			cpr.setNotifyTxt(orderData);
			// 如果已经有凭证不再进行下面的操作
			CmbcPayRecord cmbc = cmbcPayRecordService.getByOrderNo(ss[0]);
			if(cmbc == null){
				// 保存订单凭证到文档
				cmbcPayRecordService.save(cpr);
				// 校验是否能支付
				cmbcPayRecordService.checkSuccess(ss[0], ss[3], ss[6]);
			}
		} catch (DecryptKitException e) {
			logger.error("密钥反初始化失败", e);
		} catch (Exception e) {
			logger.error("接受返回参数失败", e);
		}
	}
	
	
}
