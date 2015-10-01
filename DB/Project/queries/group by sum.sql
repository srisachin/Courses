insert into crawl(
select
    crawls.songId,
    max(views1) as views1,
    max(views2) as views2,
    max(views3) as views3,
    max(views4) as views4,
    max(views5) as views5,
    max(views6) as views6
  from crawls
  group by songId);