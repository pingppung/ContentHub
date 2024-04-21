insert into site (name, url)
select '카카오페이지', 'https://page.kakao.com'
where not exists (select 1 from site where name = '카카오페이지');

insert into site (name, url)
select '네이버시리즈', 'https://series.naver.com'
where not exists (select 1 from site where name = '네이버시리즈');
