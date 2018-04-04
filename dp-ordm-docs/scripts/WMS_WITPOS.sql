/**
*
*
*
**/

use AIS20130314203706
--ȥ���������еĻ��з���
update t_barcode set fbarcode = replace(fbarcode,char(10),'') where charindex(char(10),fbarcode) > 0

--������ϸ
insert into pos.ERP_WITPOS.dbo.I_ERP_InvoiceDetail(
	ERPTicketCode,
	Barcode,
	Unitcode,
	Quantity,Price,
	DisCode,
	SynFlag,
	PutTime,
	BeginGetTime,
	EndGetTime)
select h.fbillno,
	coalesce(b.FBarCode,''),
	i.FItemID,sum(d.FAuxQty),
	0,
	'',
	0,getdate(),
	null,
	null
from [AIS20130314203706].[dbo].[ICStockBill] h 
join [AIS20130314203706].[dbo].[ICStockBillEntry] d on h.FInterID = d.FInterID and h.FTranType = 21 and h.FCheckerID > 0 
join [AIS20130314203706].[dbo].[t_ICItem] i on d.FItemID = i.FItemID
left join t_BarCode b on b.FItemID = d.FItemID and b.FTypeID = 4
join pos.ERP_WITPOS.dbo.I_ERP_Products pt on i.FItemID=pt.unitcode and pt.brand='ZW'
left JOIN [AIS20130314203706].[dbo].[t_Item] ot on h.FHeadSelfB0164 = ot.FItemID
join t_Item_3008 t3008 on h.FHeadSelfB0168=t3008.FItemID
where (t3008.F_123 ='' or t3008.F_123 is null)
and (h.FHeadSelfB0167 ='' or h.FHeadSelfB0167 is null)
and ot.fname='����'
and h.FROB = 1
and h.FHeadSelfB0168 <>'' and h.FHeadSelfB0168 is not null
group by h.FBillNo,h.FHeadSelfB0165,h.FBillNo,coalesce(b.FBarCode,''),i.FItemID

--������
insert into pos.ERP_WITPOS.dbo.I_ERP_Invoices(Brand,ERPTicketCode,TotalQuantity,TotalAmount,Direction,
CounterCode,Dealer,GenerateTime,DisCode,PreTicketCode,SynFlag,PutTime,BeginGetTime,EndGetTime,c_Catagory)
select 'ZW',h.fbillno,SUM(d.FAuxQty),0,0,t3008.F_110,'',h.FHeadSelfB0165,'000',null,0,GETDATE(),null,null,null  
from [AIS20130314203706].[dbo].[ICStockBill] h 
join [AIS20130314203706].[dbo].[ICStockBillEntry] d on h.FInterID = d.FInterID and h.FTranType = 21 and h.FCheckerID > 0 
join [AIS20130314203706].[dbo].[t_ICItem] i on d.FItemID = i.FItemID 
join pos.ERP_WITPOS.dbo.I_ERP_Products pt on i.FItemID=pt.unitcode and pt.brand='ZW'
left JOIN [AIS20130314203706].[dbo].[t_Item] ot on h.FHeadSelfB0164 = ot.FItemID
join t_Item_3008 t3008 on h.FHeadSelfB0168=t3008.FItemID
where (t3008.F_123 ='' or t3008.F_123 is null)
and (h.FHeadSelfB0167 ='' or h.FHeadSelfB0167 is null)
and ot.fname='����'
and h.FROB = 1
and h.FHeadSelfB0168 <>'' and h.FHeadSelfB0168 is not null
group by h.FBillNo,t3008.F_110,h.FHeadSelfB0165

--�޸ĵ�����ʶ
update h 
set FHeadSelfB0167 = '��������-' + h.FBillNo,
	-- FHeadSelfD0143 = convert(nvarchar,GETDATE(),120)
from ICStockBill h
left JOIN [AIS20130314203706].[dbo].[t_Item] ot on h.FHeadSelfB0164 = ot.FItemID
join t_Item_3008 t3008 on h.FHeadSelfB0168=t3008.FItemID
where (t3008.F_123 ='' or t3008.F_123 is null)
and (h.FHeadSelfB0167 ='' or h.FHeadSelfB0167 is null)
and ot.fname='����'
and h.FROB = 1
and h.FHeadSelfB0168 <>'' and h.FHeadSelfB0168 is not null



