SELECT i.fnumber,i.F_112,i.FName,
SUM(d.FAuxQty) as 'Qty'
FROM [AIS20130314203706].[dbo].[ICStockBill] h 
JOIN [AIS20130314203706].[dbo].[ICStockBillEntry] d on h.FInterID = d.FInterID
JOIN [AIS20130314203706].[dbo].[t_ICItem] i on d.FItemID = i.FItemID
JOIN [AIS20130314203706].[dbo].[t_Stock] s on s.FItemID = d.FSCStockID
where h.frob=1 and h.fstatus=0 and h.FCancellation=0
and i.f_112 is not null
and h.FTranType =41
and s.fnumber in ('3.01','3.03','3.04','3.05','3.06','4.01','4.03','4.04','4.05','4.06')
group by i.fnumber ,i.f_112,i.fname,h.fstatus
UNION ALL

SELECT i.fnumber,i.F_112,i.FName,
SUM(d.FAuxQty) as 'Qty'
FROM [AIS20130314203706].[dbo].[ICStockBill] h 
JOIN [AIS20130314203706].[dbo].[ICStockBillEntry] d on h.FInterID = d.FInterID
JOIN [AIS20130314203706].[dbo].[t_ICItem] i on d.FItemID = i.FItemID
JOIN [AIS20130314203706].[dbo].[t_Stock] s on s.FItemID = d.FDCStockID
where h.frob=1 and h.fstatus=0 and h.FCancellation=0
and i.f_112 is not null
and h.FTranType =21
and s.fnumber in ('3.01','3.03','3.04','3.05','3.06','4.01','4.03','4.04','4.05','4.06')
group by i.fnumber ,i.f_112,i.fname,h.fstatus


SELECT * FROM T_FieldDescription WHERE FDescription LIKE '%×÷·Ï%'   --×Ö¶Î±í