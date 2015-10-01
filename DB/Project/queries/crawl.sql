insert into lcrawl
(
select
    crawlslim.songId,
    sum(views121) as views121,
    sum(views122) as views122,
    sum(views123) as views123,
    sum(views124) as views124,
    sum(views205) as views205,
    sum(views206) as views206,
    sum(views207) as views207,
    sum(views208) as views208,
    sum(views210) as views210,
    sum(views211) as views211,
    sum(views213) as views213,
    sum(views214) as views214,
    sum(views216) as views216,
    sum(views221) as views221,
    sum(views226) as views226,
    sum(views228) as views228,
    sum(views301) as views301,
    sum(views302) as views302,
    sum(views303) as views303,
	sum(views306) as views306,
	sum(views312) as views312,
	sum(views314) as views314,
	sum(views315) as views315
  from crawlslim
  group by songId
  )

	
	