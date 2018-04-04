select  distinct h.FBillNo as 'OutorderNO' 
,a.fname as 'specialNO','MH' as 'OwnerCode','XSDN' as 'OrderType',h.FHeadSelfB0165 as 'OrderTime',ogt.FName as 'CustomerType',a.FContact as 'Receiver', 
cast(isnull(a.FPhone,'') as nvarchar(20))+','+cast(isnull(a.FMobilePhone,'') as nvarchar(20)) as 'ReceiverTel', 
b.FName as 'ReceiverProvince',c.FName as 'ReceiverCity',a.F_106 as 'ReceiverCounty',a.FAddress as 'ReceiverAddress', 
SUM(d.FAuxQty*i.F_102) over (partition by h.FBillNo) as 'TotalAmount',h.FExplanation as 'Remark', 
Row_Number() over (partition by h.FBillNo order by h.FBillNo) as 'LineNum',i.F_112 as 'ProductCode', 
i.FName as 'ProductName',i.FModel as 'Specification',i.F_101  as 'Manufacturers',SUM(d.FAuxQty) as 'Qty',i.F_102 as 'Price', 
case when s.fnumber like N'3.%' then N'BJ001' when s.fnumber like N'4.%' then N'GZ001' end  as N'partner' 
from AIS20170111150137.[dbo].[ICStockBill] h 
join AIS20170111150137.[dbo].[ICStockBillEntry] d on h.FInterID = d.FInterID and h.FTranType = 21 and h.FCheckerID > 0 
join AIS20170111150137.[dbo].[t_ICItem] i on d.FItemID = i.FItemID 
join AIS20170111150137.[dbo].[t_Organization] a on a.FItemID = h.FSupplyID 
join AIS20170111150137.[dbo].[CRM_Province] b on a.FProvinceID = b.FID 
join AIS20170111150137.[dbo].[CRM_City] c on a.FCityID = c.FID 
JOIN AIS20170111150137.[dbo].[t_Item_3017] ogt on a.F_107 = ogt.FItemID
join AIS20170111150137.[dbo].[t_Stock] s on s.FItemID = d.FDCStockID 
left JOIN AIS20170111150137.[dbo].[t_Item] ot on h.FHeadSelfB0164 = ot.FItemID 
WHERE h.fbillno=''  --h.FHeadSelfB0165 >= ? and h.FHeadSelfB0165 &lt; ?
and (s.fnumber like N'3.%' or s.fnumber like N'4.%') 
and (h.FHeadSelfB0163 = '' or h.FHeadSelfB0163 is null)
and (h.FHeadSelfB0168 <> '' or h.FHeadSelfB0168 is not null)
and ot.FName = '正常'
and h.FROB = 1
GROUP BY h.fbillno,a.fname,h.FHeadSelfB0165,a.fcontact,ogt.FName,
cast(isnull(a.FPhone,'') as nvarchar(20))+','+cast(isnull(a.FMobilePhone,'') as nvarchar(20)), 
h.fexplanation,i.fname,i.F_112,i.fmodel,i.f_101,d.fauxqty,i.F_102,b.fname,c.fname,a.f_106,a.faddress,s.fnumber


SELECT DISTINCT h.FBillNo AS 'OutorderNO'
,t3008.FName as 'specialNO','MH' as 'OwnerCode','MDHN' as 'OrderType',h.FHeadSelfD0146 as 'OrderTime',ogt.FName as 'CustomerType',a.FContact as 'Receiver', 
cast(isnull(a.FPhone,'') as nvarchar(20))+','+cast(isnull(a.FMobilePhone,'') as nvarchar(20)) as 'ReceiverTel', 
b.FName as 'ReceiverProvince',c.FName as 'ReceiverCity', 
a.F_106 as 'ReceiverCounty', a.FAddress as 'ReceiverAddress',SUM(d.FAuxQty*i.F_102) over (partition by h.FBillNo) as 'TotalAmount', 
'' as 'Remark',Row_Number() over (partition by h.FBillNo order by h.FBillNo) as 'LineNum', 
i.F_112 as 'ProductCode',i.FName as 'ProductName',i.FModel as 'Specification', 
i.F_101  as 'Manufacturers',SUM(d.FAuxQty) as 'Qty',i.F_102 as 'Price', 
case when s.fnumber like N'3.%' then N'BJ001' when s.fnumber like N'4.%' then N'GZ001' end  as N'partner' 
FROM [AIS20130314203706].[dbo].[ICStockBill] h 
JOIN [AIS20130314203706].[dbo].[ICStockBillEntry] d on h.FInterID = d.FInterID and h.FTranType = 41 and h.FCheckerID > 0 
JOIN [AIS20130314203706].[dbo].[t_Item_3008] t3008 on d.FDCStockID = t3008.F_108 
JOIN [AIS20130314203706].[dbo].[t_Item] t3007 on t3008.F_101 = t3007.FItemID and t3007.FName = '直营' 
JOIN [AIS20130314203706].[dbo].[t_Item] t3011 on h.FHeadSelfD0140 = t3011.FItemID and t3011.FItemClassID = 3011 and t3011.FName = '发货调拨' 
JOIN [AIS20130314203706].[dbo].[t_Organization] a on t3008.FNumber=a.FNumber 
JOIN [AIS20130314203706].[dbo].[CRM_Province] b on a.FProvinceID = b.FID 
JOIN [AIS20130314203706].[dbo].[CRM_City] c on a.FCityID = c.FID 
JOIN [AIS20130314203706].[dbo].[t_Item_3017] ogt on a.F_107 = ogt.FItemID
JOIN [AIS20130314203706].[dbo].[t_ICItem] i on d.FItemID = i.FItemID
JOIN [AIS20130314203706].[dbo].[t_Stock] s on s.FItemID = d.FSCStockID
left JOIN [AIS20130314203706].[dbo].[t_Item] ot on h.FHeadSelfD0145 = ot.FItemID 
WHERE h.fbillno='BDD201701180002'--h.FHeadSelfD0146 >= ? and h.FHeadSelfD0146 &lt; ? 
and (s.fnumber like N'3.%' or s.fnumber like N'4.%') 
and ot.FName in ('正常','试用装')
GROUP BY h.FBillNo,t3008.FName,h.FHeadSelfD0146,a.FContact,ogt.FName,
cast(isnull(a.FPhone,'') as nvarchar(20))+','+cast(isnull(a.FMobilePhone,'') as nvarchar(20)), 
b.FName,c.FName,a.F_106,a.FAddress,i.FName,i.F_112,i.FModel,i.F_101,d.FAuxQty,i.F_102,s.fnumber  

--select * from AIS20170111150137.[dbo].[ICStockBill] where fbillno='201701140008'
