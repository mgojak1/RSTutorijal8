create table busses(
                       id integer primary key ,
                       maker text,
                       series text,
                       seatNumber integer,
                       driverOne integer,
                       driverTwo integer
);

create table drivers(
                        id integer primary key ,
                        name text,
                        surname text,
                        umcn text,
                        birthday date,
                        hireDate date
);