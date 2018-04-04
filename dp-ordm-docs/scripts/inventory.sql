select s.FName,i.fstockid,
t.FItemID,t.FName,sum(i.FQty) as Qty
from [AIS20130314203706].[dbo].ICInventory i
join [AIS20130314203706].[dbo].[t_ICItem] t on t.fitemid = i.fitemid 
join [AIS20130314203706].[dbo].t_Stock s on s.fitemid = i.fstockid
where s.fnumber in ('3.01','3.03','3.04','3.05','3.06','4.01','4.03','4.04','4.05','4.06')
--and i.FQty<>0
group by t.FItemID,t.FName,s.FName,i.fstockid