select 
t.FItemID,t0.FNumber,t.FNumber,t.FName,t1.FBarCode,t.F_112 as LogisticsCode
,t.F_102 as MemberPrice,F_103 as MaterialPrice,t.F_101 as Manufacturer,t.FModel as Specifications,t.FSize,t.FLength
,t.FWidth,t.FHeight,t.f_111 as BoxNum,t.FDeleted,null as TypeID,null as image,GETDATE() as LastUpdateTime--t.FOrderPrice
from [AIS20130314203706].[dbo].[t_ICItem] t
join [AIS20130314203706].[dbo].[t_Item] t0 on t.F_105 = t0.FItemID
left join [AIS20130314203706].[dbo].[t_BarCode] t1 on t1.FItemID = t.FItemID
where  t.FDeleted = 0
and F_112 is not null