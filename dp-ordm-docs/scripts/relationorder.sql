--����ֱ��ϵͳ������
Declare @relationorder  varchar(45)
SET @relationorder='201702230008'

--ֱ��ϵͳ��������۵� ���Ź���
select v1.orderno as 'ֱ��ϵͳ����'
,case when v1.orderstatus =6 then N'�����͵����' else '����' end as 'ֱ��ϵͳ״̬'
,v1.id as 'ֱ��ϵͳ��������'
--,v1.statu as 'statu'
,o.fbillno as '���ϵͳ����'
,case when o.FStatus ='1' then N'�����' else 'δ����' end as '�������״̬'
,case when v2.is_success='1' then N'������' else 'δ����' end as '���͵�����״̬'
,case when (o.FHeadSelfB0167 <>'' or o.FHeadSelfB0167 is not null) then '������' else 'δ����' end  as '���͵�����ͨ״̬'
from OPENQUERY (WMS_MYSQLNEW, 
	'select a.orderno as orderno,b.id as id,a.orderstatus as orderstatus,b.status as statu from bus_order a 
	left join bus_package b on a.orderno = b.orderno') v1
left join [AIS20130314203706].[dbo].[ICStockBill] o on v1.orderno=o.FHeadSelfB0170 and o.ftrantype=21
left join OPENQUERY (WMS_MYSQL, 
	'select no,is_success from ldws2.ws_outorderresponse') v2 on o.fbillno=v2.no
where v1.orderno = @relationorder

--�ܲ�ֱӪ��Ҫ�𵥳ɵ����������۵�,���۵�ͬ�ϣ�������ͬ��
--ֱ��ϵͳ����������� ���Ź���
union all
select v1.orderno as 'ֱ��ϵͳ����'
,case when v1.orderstatus =6 then N'�����͵����' else '����' end as 'ֱ��ϵͳ״̬'
,v1.id as 'ֱ��ϵͳ��������'
--,v1.statu as 'statu'
,o.fbillno as '���ϵͳ����'
,case when o.FStatus ='1' then N'�����' else 'δ����' end as '�������״̬'
,case when v2.is_success='1' then N'������' else 'δ����' end as '���͵�����״̬'
,case when (o.FHeadSelfB0167 <>'' or o.FHeadSelfB0167 is not null) then '������' else 'δ����' end  as '���͵�����ͨ״̬'
from OPENQUERY (WMS_MYSQLNEW, 
	'select a.orderno as orderno,b.id as id,a.orderstatus as orderstatus,b.status as statu from bus_order a 
	left join bus_package b on a.orderno = b.orderno') v1
left join [AIS20130314203706].[dbo].[ICStockBill] o on v1.orderno=o.FHeadSelfD0134 and o.ftrantype=41
left join OPENQUERY (WMS_MYSQL, 
	'select no,is_success from ldws2.ws_outorderresponse') v2 on o.fbillno=v2.no
where v1.orderno = @relationorder