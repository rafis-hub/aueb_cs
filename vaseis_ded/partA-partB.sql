/*ratings*/
create table Ratings_Small(
   userId int,
   movieId int,
   rating varchar(10),
   timestamp int
);
alter table Ratings_Small add primary key (userid, movieid);
--για τη διαγραφη διπλοτυπων χρησιμοποιηθηκε ενας πινακας τεστ οπου σε αυτον
--ειχαν ανεβει τα δεδομενα

/*keywords*/
create table Keywords_test(
   id int,
   keywords text);

create table Keywords(
   id int,
   keywords text);

insert into Keywords(id)
select distinct Keywords_test.id
from Keywords_test;

update Keywords
set keywords = Keywords_test.keywords
from Keywords_test
where Keywords.id = Keywords_test.id;

alter table Keywords add primary key (id);

/*credits*/
create table Credits_test(
   _cast text,
   crew text,
   id int
);

create table Credits(
   _cast text,
   crew text,
   id int
);

insert into Credits(id)
select distinct Credits_test.id
from Credits_test;

update Credits
set _cast = Credits_test._cast , crew = Credits_test.crew
from Credits_test
where Credits.id = Credits_test.id;

alter table Credits add primary key (id);

/*links*/
create table Links_test(
   movieId int,
   imdbId int,
   tmdbId int
);

create table Links(
   movieId int,
   imdbId int,
   tmdbId int
);

insert into Links(movieid)
select distinct Links_test.movieid
from Links_test;

update Links
set imdbid = Links_test.imdbid , tmdbid = Links_test.tmdbid
from Links_test
where Links.movieid = Links_test.movieid;

alter table Links add primary key (movieid);

/*movies metadata*/
create table Movies_metadata_test(
   adult boolean,
   belongs_to_collection varchar(190),
   budget int,
   genres text,
   homepage varchar(250),
   id int,
   imdb_id varchar(10),
   original_language varchar(10),
   original_title varchar(110),
   overview varchar(1000),
   popularity numeric,
   poster_path varchar(40),
   production_companies varchar(1260),
   production_countries varchar(1040),
   release_date date,
   revenue bigint,
   runtime numeric,
   spoken_languages varchar(770),
   status varchar(20),
   tagline varchar(300),
   title varchar(110),
   video boolean,
   vote_average numeric,
   vote_count int
);

create table Movies_Metadata(
   adult boolean,
   belongs_to_collection varchar(190),
   budget int,
   genres text,
   homepage varchar(250),
   id int,
   imdb_id varchar(10),
   original_language varchar(10),
   original_title varchar(110),
   overview varchar(1000),
   popularity numeric,
   poster_path varchar(40),
   production_companies varchar(1260),
   production_countries varchar(1040),
   release_date date,
   revenue bigint,
   runtime numeric,
   spoken_languages varchar(770),
   status varchar(20),
   tagline varchar(300),
   title varchar(110),
   video boolean,
   vote_average numeric,
   vote_count int
);

insert into Movies_metadata(id)
select distinct Movies_metadata_test.id
from Movies_metadata_test;

update Movies_metadata
set adult=Movies_metadata_test.adult ,
belongs_to_collection =Movies_metadata_test.belongs_to_collection ,
budget=Movies_metadata_test.budget ,
genres=Movies_metadata_test.genres ,
homepage=Movies_metadata_test.homepage ,
imdb_id=Movies_metadata_test.imdb_id ,
original_language=Movies_metadata_test.original_language ,
original_title=Movies_metadata_test.original_title ,
overview=Movies_metadata_test.overview ,
popularity=Movies_metadata_test.popularity ,
poster_path=Movies_metadata_test.poster_path ,
production_companies=Movies_metadata_test.production_companies ,
production_countries=Movies_metadata_test.production_countries ,
release_date=Movies_metadata_test.release_date ,
revenue=Movies_metadata_test.revenue ,
runtime=Movies_metadata_test.runtime ,
spoken_languages=Movies_metadata_test.spoken_languages ,
status=Movies_metadata_test.status ,
tagline=Movies_metadata_test.tagline ,
title=Movies_metadata_test.title ,
video=Movies_metadata_test.video ,
vote_average=Movies_metadata_test.vote_average ,
vote_count=Movies_metadata_test.vote_count
from Movies_metadata_test
where Movies_metadata.id = Movies_metadata_test.id;

alter table Movies_metadata add primary key (id);

alter table Movies_metadata add foreign key (id)
references Keywords(id);

alter table Movies_metadata add foreign key (id)
references Credits(id);

alter table ratings_small add foreign key (movieid)
references movies_metadata(id);

alter table links add foreign key (movieid)
references movies_metadata(id);
/*delete from metadata*/
delete from ratings_small
where movieid NOT IN(select movies_metadata.id
from ratings_small 
join movies_metadata 
on ratings_small.movieid=movies_metadata.id)

delete from Links
where movieid NOT IN(select movies_metadata.id
from Links 
join movies_metadata M
on Links.movieid=movies_metadata.id)

/*καταλληλη επεξεργασια του moivies_metadata*/
update movies_metadata
set genres = replace(movies_metadata.genres, '[', ''); 

update movies_metadata
set genres = replace(movies_metadata.genres, ']', '');

update movies_metadata
set genres = replace(movies_metadata.genres, '{', '');

update movies_metadata
set genres = replace(movies_metadata.genres, '}', '');
/*PART B*/
/*tainies ana xrono*/
select count(id), extract(year from movies_metadata.release_date) as year
from movies_metadata
group by year
order by year;
/*arithmos tainiwn ana eidos*/
select substring(T.genre,9), count(T.movieID)
from(SELECT regexp_split_to_table(genres, ',') AS genre, id AS movieID
from movies_metadata) as T
WHERE T.genre NOT LIKE '%id%'
group by T.genre;
/*arithmos tainiwn an eidos kai xrono*/
SELECT T.genre,  T.year, count(T.movieID) AS number_Of_Movies
FROM (SELECT regexp_split_to_table(genres, ',') AS genre, id AS movieID, extract(year from movies_metadata.release_date) as year
		from movies_metadata) AS T
WHERE T.genre NOT LIKE '%id%' 
GROUP BY T.genre, year;
/*ai8mos tainiwn ana eidos kai avg rating*/
select substring(T.genre,9), count(T.movieID), avg(ratings_small.rating::numeric)
from(SELECT regexp_split_to_table(genres, ',') AS genre, id AS movieID
from movies_metadata) as T inner join ratings_small on T.movieID = ratings_small.movieid
WHERE T.genre NOT LIKE '%id%'
group by T.genre;
/*count ari8mos rating ana xristi*/
select userid ,count(movieid) as count
from Ratings
group by userid
order by userid;
/*mesi ba8mologia ana xristi*/
select userid ,avg(cast(rating as numeric)) 
from Ratings
group by userid ;

/*creating and insert data on view_table */
create table View_table(
	id int,
	ratings_count bigint,
	ratings_average numeric
);

insert into View_table(id,ratings_count,ratings_average)
select userid ,count(movieid) as count, avg(rating::numeric) as average
from Ratings
group by userid;

alter table View_table add primary key (id);
alter table Ratings_small add foreign key (userid)
references View_table(id);