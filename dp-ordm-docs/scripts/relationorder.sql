--声明直发系统订单号
Declare @relationorder  varchar(45)
SET @relationorder='201702230008'

--直发系统到金蝶销售单 单号关联
select v1.orderno as '直发系统单号'
,case when v1.orderstatus =6 then N'已推送到金蝶' else '其他' end as '直发系统状态'
,v1.id as '直发系统包裹单号'
--,v1.statu as 'statu'
,o.fbillno as '金蝶系统单号'
,case when o.FStatus ='1' then N'已审核' else '未处理' end as '金蝶处理状态'
,case when v2.is_success='1' then N'已推送' else '未推送' end as '推送到物流状态'
,case when (o.FHeadSelfB0167 <>'' or o.FHeadSelfB0167 is not null) then '已推送' else '未推送' end  as '推送到店务通状态'
from OPENQUERY (WMS_MYSQLNEW, 
	'select a.orderno as orderno,b.id as id,a.orderstatus as orderstatus,b.status as statu from bus_order a 
	left join bus_package b on a.orderno = b.orderno') v1
left join [AIS20130314203706].[dbo].[ICStockBill] o on v1.orderno=o.FHeadSelfB0170 and o.ftrantype=21
left join OPENQUERY (WMS_MYSQL, 
	'select no,is_success from ldws2.ws_outorderresponse') v2 on o.fbillno=v2.no
where v1.orderno = @relationorder

--总部直营需要拆单成调拨单和销售单,销售单同上，调拨单同下
--直发系统到金蝶调拨单 单号关联
union all
select v1.orderno as '直发系统单号'
,case when v1.orderstatus =6 then N'已推送到金蝶' else '其他' end as '直发系统状态'
,v1.id as '直发系统包裹单号'
--,v1.statu as 'statu'
,o.fbillno as '金蝶系统单号'
,case when o.FStatus ='1' then N'已审核' else '未处理' end as '金蝶处理状态'
,case when v2.is_success='1' then N'已推送' else '未推送' end as '推送到物流状态'
,case when (o.FHeadSelfB0167 <>'' or o.FHeadSelfB0167 is not null) then '已推送' else '未推送' end  as '推送到店务通状态'
from OPENQUERY (WMS_MYSQLNEW, 
	'select a.orderno as orderno,b.id as id,a.orderstatus as orderstatus,b.status as statu from bus_order a 
	left join bus_package b on a.orderno = b.orderno') v1
left join [AIS20130314203706].[dbo].[ICStockBill] o on v1.orderno=o.FHeadSelfD0134 and o.ftrantype=41
left join OPENQUERY (WMS_MYSQL, 
	'select no,is_success from ldws2.ws_outorderresponse') v2 on o.fbillno=v2.no
where v1.orderno = @relationorder