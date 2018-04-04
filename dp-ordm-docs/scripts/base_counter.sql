delete OPENQUERY(WMS_MYSQLNEW,'select * from base_counter')
INSERT INTO OPENQUERY(WMS_MYSQLNEW,'select counterid,countercode,countername,customerid
,contact,mobile,phone,country,province,city,areacounty,areaprice,address
,shipping_address,postcode,channel,type,status,lastupdate from base_counter')


select t3008.FItemID as 'CounterID'
,t3008.F_110 as 'CounterCode'
,t3008.FName as 'CounterName'
,F_109 as 'customerid'
,F_115 as 'Contact'
,F_117 as 'Mobile'
,F_116 as 'Phone'
,N'�й�' as 'Country'
,p.FName as 'Province'
,c.fName as 'City'
,F_120 as 'AreaCounty'
,F_106 as 'AreaPrice'
,F_118 as 'Address'
,F_118 as 'Shipping_Address'
,F_119 as 'PostCode'
,F_105 as 'Channel'
,t3007.FName as 'Type'
,s.FDeleted as 'Status'
,cast(getdate() as bigint) as 'lastUpdate'
from [AIS20130314203706].[dbo].[t_Item_3008] t3008
JOIN [AIS20130314203706].[dbo].[t_Item] t3007 on t3008.F_101 = t3007.FItemID --�ŵ�����
JOIN [AIS20130314203706].[dbo].[t_Item] s on t3008.FItemID=s.FItemID --״̬
JOIN [AIS20130314203706].[dbo].[t_Item] p on t3008.F_103 = p.FItemID and p.FItemClassID='3009' --ʡ��
JOIN [AIS20130314203706].[dbo].[t_Item] c on t3008.F_104 = c.FItemID and c.FItemClassID='3010' --����
where s.FDeleted=0
and t3008.FName not like '%��%'
and p.FName in ('����ʡ')
and F_109 <>0