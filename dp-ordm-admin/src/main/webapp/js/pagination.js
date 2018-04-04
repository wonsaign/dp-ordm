$(document).ready(function(){
	$('#myDiv').on('click','.page_btn',function(){
		var src=$('[name="paginationURL"]').text();
		var key=$('[name="key"]').text();
		var nowpage=parseInt($('[name="page"]').text());
		var max=parseInt($('[name="max"]').text());
		var jumppage=$('[name="jumppage"]').val();
		var position=$(this);
		var page=1;
		var num=10;
		var flag=true;
		
		if(position.attr('name')=="firstpage"&&nowpage==1){
			flag=false;
		}else if(position.attr('name')=="prevpage"){
			if(nowpage>1){
				page=parseInt(nowpage)-1;
			}else{
				flag=false;
			}
		}else if(position.attr('name')=="nextpage"){
			if(nowpage<max){
				page=parseInt(nowpage)+1;
			}else{
				flag=false;
			}
		}else if(position.attr('name')=="lastpage"){
			if(page!=max){
				page=max;
			}else{
				flag=false;
			}
		}else if(position.attr('name')=="jump"){
			if(jumppage<=max&&jumppage>0&&jumppage!=nowpage){
				page=jumppage;
			}else{
				flag=false;
			}
		}
		var VfsPrefix=$('[name="VfsPrefix"]').text();
		if(flag){
			$.ajax({
				type:"post",
				url:src+".do",
				data:{page:page,num:num,key:key},
				dataType: "json",
				async:true,
				success: function(data) {
					if(data!=null){
						var table=$('[name="table_position"]');
						table.children().find('tr[name="table_body"]').remove();
						$.each(data,function(index, value){
							var htmlsel;
							if(value!=null){
								//客户
								if(src.indexOf("customer")>0){
									htmlsel="<tr name='table_body'><td>"+value.customerID+"</td><td>"+value.customerName+"</td>"
									+"<td>"+value.contact+"</td><td>"+value.mobile+"</td><td>"+value.customerType+"</td>"
									+"<td>"+value.postCode+"</td><td>"+value.lastUpdate+"</td><td>"+value.status+"</td></tr>";
								}else if(src.indexOf("counter")>0){
								//柜台
									htmlsel="<tr name='table_body'><td>"+value.counterCode+"</td><td>"+value.counterName+"</td>"
									+"<td>"+value.customerName+"</td><td>"+value.contact+"</td><td>"+value.mobile+"</td>"
									+"<td>"+value.address+"</td><td>"+value.type+"</td><td>"+value.status+"</td><td>"+value.lastUpdate+"</td></tr>";
								}else if(src.indexOf("PRP")>0){
								//产品关联
									htmlsel="<tr name='table_body'><td><a class='prp_amend' href='../productpolicy/updatebtn.do?policyId="
										+value.productRelationPolicy.policyId+"' target='_blank'>修改</a> ";
									if(value.productRelationPolicy.status==0){
										htmlsel+="<span class='prp_start' href='../productpolicy/enable'>启用</span>"
											+"<span class='prp_down'href='../productpolicy/disable' hidden='hidden'>禁用</span></td>";
									}else{
										htmlsel+="<span class='prp_start' href='../productpolicy/enable' hidden='hidden'>启用</span>"
											+"<span class='prp_down'href='../productpolicy/disable'>禁用</span></td>";
									}
									htmlsel+="<td hidden='hidden' name='policyId'>"+value.productRelationPolicy.policyId+"</td>"
									+"<td>"+value.name+"</td><td>"+value.productName+"</td><td>"+value.type+"</td><td class='status'>"+value.status+"</td>"
									+"<td>"+value.minOrderUnit+"</td><td>"+value.associatedProducts+"</td><td>"+value.lastUpdate+"</td></tr>";
								}else if(src.indexOf("seriespage")>0){
									//系列
									htmlsel="<tr name='table_body'><td class='searchbyid' hidden='hidden'>"+value.did+"</td>"
									+"<td><a href='../product/getSeriesUrl.do?did="+value.did+"' target='_blank'>修改</a></td>"
									+"<td>"+value.hardCode+"</td><td>"+value.name+"</td><td>"+value.active+"</td>"
									+"<td><input class='img_rul' hidden='hidden' value="+VfsPrefix + value.url+"><span class='img_show'>"+value.haveimg+"</span></td>"
									+"<td>"+value.lastUpdate+"</td></tr>";
								}else if(src.indexOf("orderpage")>0){
									//财务待审核订单分页
									var No=(parseInt(page)-1)*parseInt(num)+parseInt(index)+1;
									htmlsel="<tr name='table_body'><td>"+No+"<input type='hidden' value="+value.id+" class='orderid'></td>" 
										+"<td style='min-width:70px'><span class='order_pass' href='../orderadm/orderPass'>通过</span> "
										+"<span class='order_refuse'>退回</span></td><td>"+value.orderNo+"</td>"
										+"<td>"+value.counterName+"</td><td>"+value.customerName+"</td>";
									if(value.paymentFee!=(parseFloat(value.paymentPrice)+parseFloat(value.order.useBalance))){
										htmlsel+="<td style='color: red'>"+value.paymentFee+"</td>" +
												"<td style='color: red'>"+value.paymentPrice+"</td>"+
												"<td style='color: red'>"+value.order.useBalance+"</td>";
									}else{
										htmlsel+="<td>"+value.paymentFee+"</td>" +
										"<td>"+value.paymentPrice+"</td>"+
										"<td>"+value.order.useBalance+"</td>";
									}
										htmlsel+="<td>"+value.productPrice+"</td><td>"+value.expressFee+"</td><td>"+value.materialFee+"</td>"
										+"<td>"+value.payType+"</td><td>"+value.orderStatus+"</td><td>"+value.description+"</td>"
										+"<td ><div class='img_rul'  type='hidden'>";
									$.each(value.imageURL,function(index, url){
										htmlsel+="<input  type='hidden' value="+VfsPrefix + url+">";
									});
									if(value.img=="有"){
										htmlsel+="</div><span class='order_img_show' style='color: blue'>"+value.img+"</span></td>"
										+"<td>"+value.orderCreatTime+"</td></tr>";
									}else{
										htmlsel+="</div><span class='order_img_show'>"+value.img+"</span></td>"
										+"<td>"+value.orderCreatTime+"</td></tr>";
									}
								}else if(src.indexOf("checkedOrderPage")>0){
									//已经审核订单分页
									var No=(parseInt(page)-1)*parseInt(num)+parseInt(index)+1;
									htmlsel="<tr name='table_body'><td>"+No+"</td><input type='hidden' value="+value.id+" class='orderid'>"
											+"<td>"+value.orderNo+"</td><td>"+value.counterName+"</td><td>"+value.customerName+"</td>"; 
										if(value.paymentFee!=(parseFloat(value.paymentPrice)+parseFloat(value.order.useBalance))){
											htmlsel+="<td style='color: red'>"+value.paymentFee+"</td>" +
													"<td style='color: red'>"+value.paymentPrice+"</td>"+
													"<td style='color: red'>"+value.order.useBalance+"</td>";
										}else{
											htmlsel+="<td>"+value.paymentFee+"</td>" +
											"<td>"+value.paymentPrice+"</td>"+
											"<td>"+value.order.useBalance+"</td>";
										}
										htmlsel+="<td>"+value.productPrice+"</td><td>"+value.expressFee+"</td>"
										+"<td>"+value.materialFee+"</td><td>"+value.payType+"</td><td>"+value.orderStatus+"</td>"
										+"<td>"+value.description+"</td><td>"+value.checkDesc+"</td>"
										+"<td ><div class='img_rul'  type='hidden'>";
									$.each(value.imageURL,function(index, url){
										htmlsel+="<input  type='hidden' value="+VfsPrefix + url+">";
									});
									if(value.img=="有"){
									htmlsel+="</div><span class='order_img_show' style='color: blue'>"+value.img+"</span></td>"
									+"<td>"+value.orderCreatTime+"</td></tr>";
								}else{
									htmlsel+="</div><span class='order_img_show'>"+value.img+"</span></td>"
									+"<td>"+value.orderCreatTime+"</td></tr>";
								}
								}else if(src.indexOf("presentpage")>0){
									//每月赠送
									htmlsel="<tr name='table_body'><td>"+value.counterNo+"</td><td>"+value.counterName+"</td>"
										+"<td>"+value.startTime+"</td><td>"+value.endTime+"</td><td>"+value.push+"</td>"
										+"<td style='text-align: center;'><span style='color:blue;cursor: pointer;'>一键创建</span></td></tr>";
								}
								else if(src.indexOf("invalidOrderPage")>0){
									//无效订单
									htmlsel="<tr name='table_body' title='双击显示明细'><td>"
									+(parseInt(index)+1)
									+"<input type='hidden' value='"
									+value.id
									+"' class='orderid'></td><td> <span href='../orderadm/orderDelete' class='order_delete' style='color: red'>删除</span></td><td>"
									+value.orderNo
									+"</td><td>"
									+value.counterName
									+"</td><td>"
									+value.customerName;
									if(value.paymentFee!= parseFloat(value.paymentPrice)+parseFloat(value.order.useBalance)){
										htmlsel+= "</td><td style='color: red'>"+value.paymentFee+"</td>"
												+"</td><td style='color: red'>"+value.paymentPrice+"</td>"
												+"</td><td style='color: red'>"+value.order.useBalance+"</td>";
									}else{
										htmlsel+="</td><td>"+value.paymentFee+"</td>" +
										"</td><td>"+value.paymentPrice+"</td>"+
										"</td><td>"+value.order.useBalance+"</td>";
									}
									htmlsel+="<td>"+value.productPrice+"</td><td>"
									+value.expressFee+"</td><td>"
									+value.materialFee+"</td><td>"
									+value.payType+"</td><td>" +value.description +"</td>"+"<td><div class='img_rul' type='hidden'>";
									$.each(value.imageURL,function(index, url){
										htmlsel+="<input  type='hidden' value="+VfsPrefix + url+">";
									});
									if(value.img=="有"){
									htmlsel+="</div><span class='order_img_show' style='color: blue'>"+value.img+"</span></td>"
									+"<td>"+value.orderCreatTime+"</td></tr>";
									}else{
										htmlsel+="</div><span class='order_img_show'>"+value.img+"</span></td>"
										+"<td>"+value.orderCreatTime+"</td></tr>";
									}
								}else if(src.indexOf("reviewOrderPage")>0){
									//无效审核
									htmlsel="<tr name='table_body' title='双击显示明细'><td>"
									+(parseInt(index)+1)
									+"<input type='hidden' value='"
									+value.id
									+"' class='orderid'></td><td> <div href='../orderadm/orderInvalid' class='order_invalid' style='color: red'>作废</div></td><td>"
									+value.orderNo
									+"</td><td>"
									+value.counterName
									+"</td><td>"
									+value.customerName;
									if(value.paymentFee!= parseFloat(value.paymentPrice)+parseFloat(value.order.useBalance)){
										htmlsel+= "</td><td style='color: red'>"+value.paymentFee+"</td>"
												+"</td><td style='color: red'>"+value.paymentPrice+"</td>"
												+"</td><td style='color: red'>"+value.order.useBalance+"</td>";
									}else{
										htmlsel+="</td><td>"+value.paymentFee+"</td>" +
										"</td><td>"+value.paymentPrice+"</td>"+
										"</td><td>"+value.order.useBalance+"</td>";
									}
									htmlsel+="<td>"+value.productPrice+"</td><td>"
									+value.expressFee+"</td><td>"
									+value.materialFee+"</td><td>"
									+value.payType+"</td><td>"
									+value.orderStatus+"</td><td>"
									+value.description +"</td>"+"<td><div class='img_rul' type='hidden'>";
									$.each(value.imageURL,function(index, url){
										htmlsel+="<input  type='hidden' value="+VfsPrefix + url+">";
									});
									if(value.img=="有"){
									htmlsel+="</div><span class='order_img_show' style='color: blue'>"+value.img+"</span></td>"
									+"<td>"+value.orderCreatTime+"</td></tr>";
									}else{
										htmlsel+="</div><span class='order_img_show'>"+value.img+"</span></td>"
										+"<td>"+value.orderCreatTime+"</td></tr>";
									}
								}
							}
							table.append(htmlsel);
						});
					}
				}
			});
			$('[name="page"]').text(page);
			$('[name="jumppage"]').val(page);
		}
	});
});

