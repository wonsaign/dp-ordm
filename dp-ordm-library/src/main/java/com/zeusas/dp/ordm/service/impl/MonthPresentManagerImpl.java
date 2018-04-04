package com.zeusas.dp.ordm.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.google.common.base.Strings;
import com.zeusas.common.data.TypeConverter;
import com.zeusas.common.service.OnStartApplication;
import com.zeusas.core.service.ServiceException;
import com.zeusas.core.utils.AppContext;
import com.zeusas.core.utils.BeanDup;
import com.zeusas.core.utils.DateTime;
import com.zeusas.dp.ordm.entity.Counter;
import com.zeusas.dp.ordm.entity.Customer;
import com.zeusas.dp.ordm.entity.Item;
import com.zeusas.dp.ordm.entity.MonthPresent;
import com.zeusas.dp.ordm.entity.Order;
import com.zeusas.dp.ordm.entity.PresentContext;
import com.zeusas.dp.ordm.entity.Product;
import com.zeusas.dp.ordm.service.CounterManager;
import com.zeusas.dp.ordm.service.CustomerManager;
import com.zeusas.dp.ordm.service.MonthPresentManager;
import com.zeusas.dp.ordm.service.MonthPresentService;
import com.zeusas.dp.ordm.service.OrderService;
import com.zeusas.dp.ordm.service.ProductManager;
import com.zeusas.dp.ordm.service.StockService;
import com.zeusas.dp.ordm.service.SystemOrderService;
import com.zeusas.dp.ordm.task.StorehousesSyncTask;
import com.zeusas.security.auth.entity.AuthUser;
import com.zeusas.security.auth.service.AuthCenterManager;

@Service
public class MonthPresentManagerImpl extends OnStartApplication implements MonthPresentManager {

	static Logger logger = LoggerFactory.getLogger(MonthPresentManagerImpl.class);

	@Autowired
	MonthPresentService monthPresentService;
	@Autowired
	SystemOrderService systemOrderService;
	@Autowired
	OrderService orderService;
	
	private long lastUpdate;
	
	final Map<Long, MonthPresent> all_present;
	/** <yearMonth,<counterCode,MonthPresent>> <年月,<柜台号,实体>> 排除补货 */
	final Map<Integer, Map<String, MonthPresent>> yearmonth_present;

	public MonthPresentManagerImpl() {
		lastUpdate = -1;
		all_present = new HashMap<>();
		yearmonth_present = new HashMap<>();
	}

	@Override
	public synchronized void load() {
		
		Integer yearMonth =getyearmonth();
		List<MonthPresent> all = monthPresentService.findByYearMonth(yearMonth);

		all_present.clear();
		yearmonth_present.clear();
		
		for (MonthPresent monthPresent : all) {
			all_present.put(monthPresent.getId(), monthPresent);
			// 当前核算月:YYYYMM
			Integer yearmonth = monthPresent.getYearMonth();
			String counterCode = monthPresent.getCounterCode();
			
			// 构造map 以年月 柜台号索引的所有数据
			if (!yearmonth_present.containsKey(yearmonth)) {
				yearmonth_present.put(yearmonth, new HashMap<>());
			}
			yearmonth_present.get(yearmonth).put(counterCode, monthPresent);
		}
	}

	@Override
	public void onStartLoad() {
		load();
	}

	@Override
	public MonthPresent get(Long id) {
		return all_present.get(id);
	}

	@Override
	public Collection<MonthPresent> findAll() {
		return all_present.values();
	}

	@Override
	public MonthPresent findByMonthAndCode(Integer yearmonth, String counterCode) {
		if (!DateTime.checkPeriod(Calendar.HOUR, this.lastUpdate)) {
			this.load();
			this.lastUpdate = System.currentTimeMillis();
		}
		Map<String, MonthPresent> map = yearmonth_present.get(yearmonth);
		if (map == null) {
			return null;
		}
		MonthPresent monthPresent = map.get(counterCode);
		return monthPresent;
	}

	@Override
	public List<PresentContext> getUnexecutedByCounter(String counterCode) {
		Integer yearmonth = getyearmonth();
		Map<String, MonthPresent> map = yearmonth_present.get(yearmonth);
		if (map == null) {
			return null;
		}
		MonthPresent monthPresent = map.get(counterCode);
		if (monthPresent == null) {
			return null;
		}
		List<PresentContext> presentContexts = new ArrayList<>();
		for (PresentContext presentContext : monthPresent.getContext()) {
			// 未执行
			Long start=presentContext.getStartTime().getTime();
			Long now=System.currentTimeMillis();
			//FIXME:结束时间是否限制？？
			if (!presentContext.getStatus()&&now>=start) {
				presentContexts.add(presentContext);
			}
		}
		return presentContexts;
	}

	@Override
	public void excute(String counterCode, String type) throws ServiceException {
		Integer yearmonth = getyearmonth();
		Map<String, MonthPresent> map = yearmonth_present.get(yearmonth);
		if (map == null) {
			throw new ServiceException("更新月度物料状态：没有当前月模板数据");
		}
		MonthPresent cachePresent = map.get(counterCode);
		if (cachePresent == null) {
			throw new ServiceException("更新月度物料状态：获取模板错误");
		}
		for (PresentContext context : cachePresent.getContext()) {
			if (type.equals(context.getType()) && !context.getStatus()) {
				context.setStatus(true);
				break;
			}
		}
		try {
			monthPresentService.update(cachePresent);
			updateCache(cachePresent);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@Transactional
	public void excute(String orderNo, String counterCode, PresentContext presentContext) throws ServiceException {
		OrderService orderService = AppContext.getBean(OrderService.class);
		CounterManager counterManager=AppContext.getBean(CounterManager.class);
		StockService stockService=AppContext.getBean(StockService.class);
		try {
			Counter counter=counterManager.getCounterByCode(counterCode);
			Assert.notNull(counter, "柜台为空,柜台号:"+counterCode);
			String warehouses=counter.getWarehouses();
			Assert.notNull(warehouses, "柜台仓库为空,柜台号:"+counterCode);
			//没库存就不加
			for (Item item : presentContext.getItems()) {
			 	String productId=item.getId();
			 	Integer num=item.getNum();
			 	Integer qty= stockService.getStockProductQty(warehouses, productId);
			 	if(num>qty){
			 		logger.info("补货没有库存，柜台名称:{},无库存产品id:{},未发货正文:{}"//
			 				,counter.getCounterName(),productId,presentContext);
			 		return;
			 	}
			}
			orderService.addFreeMaterial(orderNo, presentContext.getItems());
			excute(counterCode, presentContext.getType());
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	private Order excuteMonthPresent(MonthPresent monthPresent) throws ServiceException {
		CounterManager counterManager = AppContext.getBean(CounterManager.class);
		CustomerManager customerManager = AppContext.getBean(CustomerManager.class);
		AuthCenterManager authCenterManager = AppContext.getBean(AuthCenterManager.class);
		StockService stockService = AppContext.getBean(StockService.class);
		// 已经被禁用
		if (!monthPresent.getStatus()) {
			return null;
		}
		List<PresentContext> contexts = monthPresent.getContext();
		List<Item> unExcuteItem = new ArrayList<>();
		// 有库存 要发货 需要更新的
		Set<Integer> indexs = new HashSet<>();

		try {
			String counterCode = monthPresent.getCounterCode();
			Counter counter = counterManager.getCounterByCode(counterCode);
			Assert.notNull(counter, "柜台为空，柜台号：" + counterCode);
			String warehou = counter.getWarehouses();
			Assert.notNull(counter, "柜台仓库id为空，柜台号：" + counterCode);

			for (PresentContext presentContext : contexts) {
				Integer index = presentContext.getIndex();
				// 已经执行 未到结束时间(系统制单时间)
				if (presentContext.getStatus()//
						|| presentContext.getEndTime().getTime() > System.currentTimeMillis()) {
					continue;
				}
				// 没库存就不加
				Boolean stock = true;
				for (Item item : presentContext.getItems()) {
					String productId = item.getId();
					Integer num = item.getNum();
					Integer qty = stockService.getStockProductQty(warehou, productId);
					if (num > qty) {
						stock = false;
					}
				}
				if (stock) {
					unExcuteItem.addAll(presentContext.getItems());
					indexs.add(index);
				}
			}
			// 该店所有月度物料都已经随单推送
			if (indexs.isEmpty()) {
				return null;
			}
			// 先更新数据库
			Customer customer = customerManager.get(counter.getCustomerId());
			AuthUser root = authCenterManager.getAuthUser("root");
			Order order = systemOrderService.buildOrder(counter, root, customer.getCustomerTypeID(), unExcuteItem);
			String orderNo = order.getOrderNo();
			// 再更新内存引用
			for (PresentContext context : monthPresent.getContext()) {
				Integer index = context.getIndex();
				if (indexs.contains(index)) {
					context.setStatus(true);
					context.setExcuteNo(orderNo);
				}
			}
			monthPresentService.update(monthPresent);
			updateCache(monthPresent);
			return order;
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}
	
	@Override
	@Transactional
	public Order excute(MonthPresent monthPresent) throws ServiceException {
		return excuteMonthPresent(monthPresent);
	}

	@Override
	public Order excute(MonthPresent monthPresent, List<String> activRecord) throws ServiceException {
		Order order = excuteMonthPresent(monthPresent);
		if(order==null){
			logger.warn("没有需要推送的物料");
			return null;
		}
		order.addActivRecord(activRecord);
		try {
			orderService.update(order);
			return order;
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public void update(MonthPresent monthPresent) throws ServiceException {
		MonthPresent cacheMP = findByMonthAndCode(monthPresent.getYearMonth(), monthPresent.getCounterCode());
		if (cacheMP == null) {
			throw new ServiceException("更新MonthPresent，获取MonthPresent失败");
		}
		MonthPresent dbMP = monthPresentService.get(cacheMP.getId());

		BeanDup.dupNotNull(monthPresent, dbMP, "context", "status", "LastUpdate");
		dbMP.setLastUpdate(new Date());
		try {
			monthPresentService.update(dbMP);
			updateCache(dbMP);
		} catch (Exception e) {
			logger.error("更新MonthPresent错误,yearmonth:{},countercode:{}", monthPresent.getYearMonth(),
					monthPresent.getCounterCode());
			throw new ServiceException(e);
		}
	}
	
	@Override
	public void saveOrUpdate(MonthPresent monthPresent,String creator) throws ServiceException {
		if(monthPresent.getContext().isEmpty()){
			throw new ServiceException("正文不能为空");
		}
		MonthPresent cacheMP = findByMonthAndCode(monthPresent.getYearMonth(), monthPresent.getCounterCode());
		try {
			if(cacheMP==null){
				monthPresent.setYearMonth(getyearmonth());
				monthPresent.setCreator(creator);
				monthPresentService.createMonthPresent(monthPresent);
				updateCache(monthPresent);
			}else{
				cacheMP.addContext(monthPresent.getContext());
				cacheMP.setLastUpdate(new Date());
				monthPresentService.update(cacheMP);
				updateCache(cacheMP);
			}
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
		
	}

	@Override
	public Map<String, MonthPresent> findByYearMonth(Integer yearmonth) {
		return yearmonth_present.get(yearmonth);
	}

	/**
	 * 获取当前核算月 20170726->201708
	 * 
	 * @return
	 */
	public Integer getyearmonth() {
		Calendar cal = Calendar.getInstance();
		int day = cal.get(Calendar.DATE);
		cal.add(Calendar.MONTH, day > 25 ? 1 : 0);
		String time = DateTime.formatDate(DateTime.YYYYMMDD, cal.getTime());
		return TypeConverter.toInteger(time.substring(0, 6));
	}

	@Override
	public Integer checkUploadFileHeard(Workbook workbook) throws ServiceException {
		ProductManager productManager = AppContext.getBean(ProductManager.class);
		Sheet sheet = workbook.getSheetAt(0);
		// 表头
		Row firstRow = sheet.getRow(0);;
		Integer lastCellNum = TypeConverter.toInteger(firstRow.getLastCellNum()-1);
		// 柜台号
		Cell counterCell = firstRow.getCell(0);
		String counterCode = counterCell.getStringCellValue();
		// 开始时间
		Cell startCell = firstRow.getCell(1);
		String start = startCell.getStringCellValue();
		// 结束时间
		Cell endCell = firstRow.getCell(2);
		String end = endCell.getStringCellValue();
		// 发货方式
		Cell DeliveryWayCell = firstRow.getCell(3);
		String DeliveryWay = DeliveryWayCell.getStringCellValue();
		if ((!"coutercode".equals(counterCode)//
				&& !"柜台号".equals(counterCode))//
				|| !"开始时间".equals(start)//
				|| !"结束时间".equals(end)//
				|| !"发货方式".equals(DeliveryWay)) {
			throw new ServiceException("表头格式错误");
		}
		for (Cell cell : firstRow) {
			String cellvalue = cell.getStringCellValue();
			if (cell.getColumnIndex() < 4//
					|| "数量".equals(cellvalue)) {
				continue;
			}
			//类型2
			if("数量".equals(firstRow.getCell(lastCellNum).getStringCellValue())){
				"产品编码".equals(firstRow.getCell(lastCellNum-1).getStringCellValue());
			}else{
				//类型1
				Product p = productManager.findByCode(cellvalue);
				if (p == null) {
					throw new ServiceException("解析表头产品错误，产品不存在，产品编码："+cellvalue);
				}
			}
			
		}
		Cell LastCell= firstRow.getCell(lastCellNum);
		
		if("数量".equals(LastCell.getStringCellValue())){
			//coutercode	开始时间	结束时间	发货方式	产品编码	数量
			return 2;
		}else {
			//coutercode	开始时间	结束时间	发货方式编码	产品编码A	产品编码B ....
			return 1;
		}
	}

	@Override
	public List<MonthPresent> readFile(Workbook workbook) throws ServiceException {
		List<MonthPresent> presents=new ArrayList<>();
		try {
			Integer type = checkUploadFileHeard(workbook);
			Sheet sheet =workbook.getSheetAt(0);
			switch (type) {
			case 1:
				presents.addAll(DateOfType1(sheet));
				break;
			case 2:
				presents.addAll(DateOfType2(sheet));
				break;
			default:
				break;
			}
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
		return presents;
	}
	/**
	 * 获取模板1的数据
	 * coutercode	开始时间	结束时间	发货方式编码	产品编码A	产品编码B ....
	 * @param sheet
	 * @return
	 */
	private List<MonthPresent> DateOfType1(Sheet sheet) throws ServiceException{
		CounterManager counterManager=AppContext.getBean(CounterManager.class);
		ProductManager productManager=AppContext.getBean(ProductManager.class);
		// <productId,ColumnIndex> 产品id 该产品在表格里的列号
		Map<Integer, String> indexOfProduct = new LinkedHashMap<>();
		// 表头
		Row firstRow = sheet.getRow(0);
		List<MonthPresent> monthPresents=new ArrayList<>(sheet.getLastRowNum()-1);
		for (Cell cell : firstRow) {
			Integer columnIndex = cell.getColumnIndex();
			if (columnIndex < 4) {
				continue;
			}
			//装配产品id与表格坐标的集合
			String code = cell.getStringCellValue();
			indexOfProduct.put(columnIndex,code);
		}
		for (Row row : sheet) {
			if(row.getRowNum()==0){
				continue;
			}
			String counterCode=row.getCell(0).getStringCellValue();
			Date start= row.getCell(1).getDateCellValue();
			Date end=row.getCell(2).getDateCellValue();
			String deliveryWayId=row.getCell(3).getStringCellValue();
			
			Counter counter=counterManager.getCounterByCode(counterCode);
			if(counter==null){
				throw new ServiceException("柜台为空，柜台号："+counterCode);
			}
			MonthPresent monthPresent=new MonthPresent(counter);
			monthPresent.setYearMonth(getyearmonth());
			
			PresentContext context =new PresentContext();
			context.setStatus(false);
			context.setStartTime(start);
			context.setEndTime(end);
			for (int i = 4; i < row.getLastCellNum(); i++) {
				Cell cell = row.getCell(i);
				Integer num = TypeConverter.toInteger(cell.getNumericCellValue());
				if(num.intValue()==0){
					continue;
				}
				String productCode = indexOfProduct.get(i);
				Product product = productManager.findByCode(productCode);
				Item item = new Item(product.getProductId().toString(), num, 0.0, deliveryWayId);
				context.addItems(item);
			}
			monthPresent.addContext(context);
			monthPresents.add(monthPresent);
		}
		return monthPresents;
	}
	/**
	 * 获取模板2的数据
	 * coutercode	开始时间	结束时间	发货方式	产品编码	数量
	 * @param sheet
	 * @return
	 */
	private List<MonthPresent> DateOfType2(Sheet sheet) throws ServiceException{
		CounterManager counterManager = AppContext.getBean(CounterManager.class);
		ProductManager productManager = AppContext.getBean(ProductManager.class);
		Map<String, MonthPresent> map = new HashMap<>();
		for (int i = 1; i < sheet.getLastRowNum(); i++) {
			Row row=sheet.getRow(i);
			String counterCode=row.getCell(0).getStringCellValue();
				Date start=row.getCell(1).getDateCellValue();
			Date end=row.getCell(2).getDateCellValue();
			String deliveryWayId=row.getCell(3).getStringCellValue();
			String productCode=row.getCell(4).getStringCellValue();
			Integer num=TypeConverter.toInteger(row.getCell(5).getNumericCellValue());
			if(num.intValue()==0){
				continue;
			}
			Product product=productManager.findByCode(productCode);
			if(product==null){
				throw new ServiceException("产品不存在,产品编码 :"+productCode);
			}
			
			if(!map.containsKey(counterCode)){
				Counter counter=counterManager.getCounterByCode(counterCode);
				if(counter==null){
					throw new ServiceException("柜台为空，柜台号："+counterCode);
				}
				MonthPresent monthPresent=new MonthPresent(counter);
				monthPresent.setYearMonth(getyearmonth());
				PresentContext context=new PresentContext();
				context.setStatus(false);
				context.setStartTime(start);
				context.setEndTime(end);
				monthPresent.addContext(context);
				map.put(counterCode, monthPresent);
			}
			Item item = new Item(product.getProductId().toString(), num, 0.0, deliveryWayId);
			map.get(counterCode).getContext().get(0).addItems(item);
		}
		return new ArrayList<>(map.values());
	}
	
	/**
	 * 更新cache
	 * @param monthPresent
	 */
	private void updateCache(MonthPresent monthPresent){
		Integer yearmonth=getyearmonth();
		String countercode=monthPresent.getCounterCode();
		Long id=monthPresent.getId();
		if (!yearmonth_present.containsKey(yearmonth)) {
			yearmonth_present.put(yearmonth, new HashMap<>());
		}
		yearmonth_present.get(yearmonth).put(countercode, monthPresent);
		all_present.put(id, monthPresent);
	}

	@Override
	public void excute(String orderNo, MonthPresent monthPresentVo) throws ServiceException {
		OrderService orderService = AppContext.getBean(OrderService.class);
		CounterManager counterManager = AppContext.getBean(CounterManager.class);
		StockService stockService = AppContext.getBean(StockService.class);
		try {
			MonthPresent monthPresent = this.get(monthPresentVo.getId());
			String counterCode = monthPresent.getCounterCode();
			Counter counter = counterManager.getCounterByCode(counterCode);
			Assert.notNull(counter, "柜台为空,柜台号:" + counterCode);
			String warehouses = counter.getWarehouses();
			Assert.notNull(warehouses, "柜台仓库为空,柜台号:" + counterCode);
			// 需要添加的物料
			List<Item> items = new ArrayList<>();
			// 有库存 要发货 需要更新的
			Set<Integer> indexs = new HashSet<>();
			for (PresentContext context : monthPresent.getContext()) {
				// 已发或者时间在开始时间前
				if (context.getStatus()//
						||!Strings.isNullOrEmpty(context.getExcuteNo())//
						||context.getStartTime().after(new Date())) {
					continue;
				}
				// 校验每一个context 中产品库存 一个产品没货 整个context不发
				boolean stock = true;
				for (Item item : context.getItems()) {
					String productId = item.getId();
					Integer num = item.getNum();
					Integer qty = stockService.getStockProductQty(warehouses, productId);
					if (num > qty) {
						logger.info("补货没有库存，柜台名称:{},无库存产品id:{},仓库:{},库存:{},未发货正文:{}"//
								, counter.getCounterName(), productId, warehouses, qty, context);
						stock = false;
						break;
					}
				}
				// 如果库存充足
				if (stock) {
					indexs.add(context.getIndex());
					items.addAll(context.getItems());
				}

			}
			if (!indexs.isEmpty()) {
				// 先更新数据库
				orderService.addFreeMaterial(orderNo, items);
				// 再更新内存引用
				for (PresentContext context : monthPresent.getContext()) {
					Integer index = context.getIndex();
					if (indexs.contains(index)) {
						context.setStatus(true);
						context.setExcuteNo(orderNo);
					}
				}
				monthPresentService.update(monthPresent);
				updateCache(monthPresent);
			}
			// 通知库存，可进行库存计算
			StorehousesSyncTask synctask = AppContext.getBean(StorehousesSyncTask.class);
			synctask.setUpdated();
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}
}
