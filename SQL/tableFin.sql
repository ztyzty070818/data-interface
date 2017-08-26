-- cmd    sqlplus / as sysdba

--select * from v$version;
--conn / as sysdba
--conn orcl/orcl


create table V_BM_SALE_STAT_D (
      "分公司名称" varchar2(255),
      "地域代码" varchar2(10),
      "日销售额" number,
      "日达成率" number,
      "日销售目标" number,
      "日差距或日超额" number,
      "月销售目标" number,
      "月累计销售额" number,
      "月累计达成率" number,
      "月累计同比增长率" number,
      "年累计销售目标" number,
      "年累计销售额" number,
      "年累计达成率" number,
      "年累计同比增长率" number
);

create table V_BM_SALE_STAT_D_BRH (
      "分公司名称" varchar2(255),
      "地域代码" varchar2(10),
      "日达成率" number,
      "月累计达成率" number,
      "月累计订货异常的专卖店数量" number
);

create table V_BM_So_STAT_D (
      "名称" varchar2(50),
      "级别" number(2),
      "代码" varchar2(30),
      "服务经理名称" varchar2(60),
      "月累计回单" number(18,2),
      "月回单目标" number(18,4),
      "月达成率" number,
      "月差距或月超额" number,
      "月累计同比" number,
      "年累计回单目标" number(18,4),
      "年累计达成率" number,
      "年累计差距或超额" number,
      "年累计同比" number,
      "月累计达成率" number(5,4),
      "月累计异常的40数量" number
);

