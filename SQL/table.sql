cmd    sqlplus / as sysdba

select * from v$version;
conn / as sysdba
conn orcl/orcl


create table salses (
      name varchar2(20),
      code varchar2(10) primary key,
      daySales number(10),
      dayRate number(5,4),
      dayTarget number(10),
      dayLess number(10),
      dayMore number(10),
      monthTarget number(10),
      monthAddupSales number(10),
      monthAddupRate number(5,4),
      monthAddupCompareRate number(5,4),
      yearAddupTarget number(10),
      yearAddupSales number(10),
      yearAddupRate number(5,4),
      yearAddupCompareRate number(5,4),
      dayGrade varchar2(10),
      monthGrade varchar2(10),
      abnormal number(10)
);

create table receipt (
      name varchar2(20),
      grade number(1),
      code varchar2(10) primary key,
      monthAddupReciept number(10),
      monthTarget number(10),
      monthRate number(5,4),
      monthLess number(10),
      monthMore number(10),
      monthAddupCompareRate number(5,4),
      yearAddupTarget number(10),
      yearAddupRate number(5,4),
      yearAddupLess number(10),
      yearAddupMore number(10),
      yearAddupCompareRate number(5,4),
      monthAddupRate number(5,4),
      monthAddupAbnormal number(10)
);