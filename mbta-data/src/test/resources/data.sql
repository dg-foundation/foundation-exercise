drop table if exists mbta_stops_facilities;
drop table if exists mbta_routes_stops;
drop table if exists mbta_lines;
drop table if exists mbta_routes;
drop table if exists mbta_stops;
drop table if exists mbta_facilities;


CREATE TABLE mbta_lines (
  id varchar(100) NOT NULL,
  long_name varchar(200) DEFAULT NULL,
  short_name varchar(100) DEFAULT NULL,
  color varchar(50) DEFAULT NULL,
  PRIMARY KEY (id)
) ;

CREATE TABLE mbta_routes (
  id varchar(100) NOT NULL,
  long_name varchar(200) DEFAULT NULL,
  short_name varchar(100) DEFAULT NULL,
  PRIMARY KEY (id)
) ;

CREATE TABLE mbta_stops (
  id varchar(100) NOT NULL,
  name varchar(100) DEFAULT NULL,
  route_id varchar(100) NOT NULL,
  municipality varchar(100) DEFAULT NULL,
  primary key(id, route_id),
  foreign key(route_id) references mbta_routes(id)
);

CREATE TABLE mbta_facilities (
  id varchar(100) NOT NULL,
  stop_id varchar(100) NOT NULL,
  type varchar(100) DEFAULT NULL,
   primary key(id),
  foreign key(stop_id) references mbta_stops(id)
);

insert into mbta_routes(id, long_name, short_name)  values('route1', 'l1', 'short1');
insert into mbta_routes(id, long_name, short_name)  values('route2', 'l2', 'short2');
insert into mbta_stops(id, name, route_id, municipality) values('s0', 'stop1', 'route2', 'Action');
insert into mbta_stops(id, name, route_id, municipality) values('s1', 'stop1', 'route1', 'Weston');
insert into mbta_stops(id, name, route_id, municipality) values('s2', 'stop2', 'route1', 'Newton');
insert into mbta_stops(id, name, route_id, municipality) values('s3', 'stop2', 'route1', 'Newton');
commit;