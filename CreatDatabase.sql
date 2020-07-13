
-- 创建数据库
create database Library;
use Library;

-- 1 图书类别表
create table category(
    id int primary key auto_increment,
    name varchar(20) not null,
    parent_id int not null
);

-- 2 图书信息表
create table bookinfo(
    book_id int primary key auto_increment,
    category_id int,
    book_name varchar(20) not null,
    author varchar(20) not null,
    price float(6,2) not null,
    press varchar(20) not null,
    public_date date not null,
    remain int not null,
    constraint fk_cate foreign key(category_id) references category(id)
)auto_increment=1000;

-- 3 登陆信息表
create table loginrecord(
    user_id int primary key auto_increment,
    phone char(11) unique key not null,
    last_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    session text default null,
    password text
)auto_increment=10000;

-- 4 读者信息表
create table user(
    user_id int primary key,
    user_name varchar(20) not null,
    sex enum('男','女','保密') default'保密',
    age char(5) default'保密',
    phone char(11) unique key not null ,
    num int default 5,
    is_ban BOOLEAN NOT NULL DEFAULT FALSE,
    is_manager BOOLEAN NOT NULL DEFAULT FALSE,
    constraint fk_lorc_id foreign key(user_id) references loginrecord(user_id),
    constraint fk_lorc_ph foreign key (phone) references loginrecord(phone)
);

-- 5 借阅信息表
create table borrowinfo(
    book_id int,
    user_id int,
    borrow_date datetime not null,
    return_date date,
    is_renew boolean default false,
    is_return boolean default false,
    real_date date,
    constraint pk_bookid_cardid primary key(book_id,user_id,borrow_date),
    constraint fk_bookid foreign key(book_id) references bookinfo(book_id),
    constraint fk_cardid foreign key(user_id) references user(user_id)
);

-- 6 预约借阅表
create table borroworder(
    book_id int,
    user_id int,
    order_date datetime default current_timestamp,
    constraint pk_bookid_cardid primary key(book_id,user_id),
    constraint fk_bo_bookid foreign key(book_id) references bookinfo(book_id),
    constraint fk_bo_cardid foreign key(user_id) references user(user_id)
);



-- 下架书籍
create trigger downbook before delete on bookinfo for each row
    begin
        delete from borrowinfo where borrowinfo.book_id = OLD.book_id;
    end;

-- 注册用户
create trigger adduser after insert on loginrecord for each row
    begin
        insert into user(user_id, user_name, phone) values(NEW.user_id, 'new_user', NEW.phone);
    end;



-- 1 插入类别记录
insert into category(name,parent_id) values('计算机',0),('历史',0),('英语',0),('科学',0),('数学',0),('艺术',0);

-- 2 插入图书记录
insert into bookinfo(category_id,book_name,author,price,press,public_date,remain) values(1,'Android权威指南','王贺',58.8,'人民邮电出版社','2016-02-01',3);
insert into bookinfo(category_id,book_name,author,price,press,public_date,remain) values(1,'linux私房菜','鸟哥',76.5,'人民邮电出版社','2015-09-21',10);
insert into bookinfo(category_id,book_name,author,price,press,public_date,remain) values(1,'Java编程思想','布鲁斯',110,'机械工业出版社','2008-05-30',12);
insert into bookinfo(category_id,book_name,author,price,press,public_date,remain) values(1,'JavaScript DOM 编程艺术','吉米科',49.0,'人民邮电出版社','2016-02-01',4);
insert into bookinfo(category_id,book_name,author,price,press,public_date,remain) values(3,'英语常用口语100句','李希',28,'南方工业出版社','2002-04-25',6);
insert into bookinfo(category_id,book_name,author,price,press,public_date,remain) values(5,'高等数学1','王志胜',45,'吉林工业出版社','2010-04-25',6);
insert into bookinfo(category_id,book_name,author,price,press,public_date,remain) values(5,'高等数学2','王志胜',48,'吉林工业出版社','2010-04-25',6);
insert into bookinfo(category_id,book_name,author,price,press,public_date,remain) values(2,'三国鼎立','吴风',55,'人民出版社','2010-09-18',6);
insert into bookinfo(category_id,book_name,author,price,press,public_date,remain) values(2,'且看盛唐','文旭烟',32,'人民出版社','2013-11-29',6);

-- 3 插读者
insert into loginrecord(phone, last_date, session, password) values ('15787223423', curtime(), '123123', '123123');
insert into loginrecord(phone, last_date, session, password) values ('15787223426', curtime(), '123123', '123123');

-- 3 更新读者信息
    -- 管理员
update user set user_name = '小明', sex = '男', age = 29, is_manager = 1 where user_id = 10000;
    -- 普通读者
update user set user_name = '李思',sex = '女', age = 24 where user_id = 10001;



-- 1 用户基础表
create view userbase as
    select user_id, phone, user_name, is_ban, is_manager
    from user;

-- 2 书籍信息表
create view bookbase(book_id, category_name, book_name, author, price, press, public_date, remain) as
    select book_id, name, book_name, author, price, press, public_date, remain
    from category, bookinfo
    where category.id = bookinfo.category_id;

-- 3 借阅信息表
create view borrowbase(book_id, book_name, user_id, borrow_date, return_date, is_renew, is_return, real_date) as
    select borrowinfo.book_id, book_name, user_id, borrow_date, return_date, is_renew, is_return, real_date
    from borrowinfo, bookinfo
    where bookinfo.book_id = borrowinfo.book_id;



-- 检测冻结状态
delimiter //
create procedure refreshis_ban (in myid int, out ban boolean)
begin

    select is_ban into ban from user where user_id = myid;

    if !ban then
        select count(*) into ban from borrowinfo where user_id = myid and is_return = false and return_date < current_date limit 1;
        update user set is_ban = ban where user_id = myid;
    end if;

end //
delimiter ;


-- 预约存储过程
delimiter //
create procedure orderbook(in myid int,in mybookid int, out result boolean)
begin

    -- 用户冻结
    declare user_is_ban boolean;
    -- 用户可借量
    declare user_num int;

    select num, is_ban into user_num, user_is_ban from user where user_id = myid;

    set autocommit=0;   # 禁止自动提交，必须commit才能提交

    insert into borroworder(book_id, user_id) values (mybookid, myid);

    if user_is_ban or user_num = 0 then
        set result = 0;
        rollback;
    else
        set result = 1;
        commit;
    end if;

end //
delimiter ;


-- 续借存储过程
delimiter //
create procedure renewbook(in myid int,in mybookid int, out result boolean)
begin

    -- 是否在借
    declare is_borrow boolean;
    -- 是否续借
    declare re_is_renew boolean;
    -- 是否冻结
    declare re_is_ban boolean;

    select count(*) into is_borrow from borrowinfo where user_id = myid and book_id = mybookid and is_return = false;
    select is_renew into re_is_renew from borrowinfo where book_id = mybookid and user_id = myid and is_return = false;
    select is_ban into re_is_ban from user where user_id =  myid;

    set autocommit=0;   # 禁止自动提交，必须commit才能提交

    update borrowinfo set is_renew = true, return_date = date_add(return_date,interval 1 month) where user_id = myid and book_id = mybookid;

    if !is_borrow or re_is_renew or re_is_ban then
        set result = 0;
        rollback;
    else
        set result = 1;
        commit;
    end if;

end //
delimiter ;


-- 借书存储过程
delimiter //    # 定义结束符
create procedure borrowbook(in myid int,in mybookid int, out result boolean)
begin

    -- 图书剩余量
    declare book_remain int;
    -- 用户可借量
    declare user_num int;
    -- 用户冻结
    declare user_is_ban boolean;
    -- 是否有预约
    declare is_order boolean;

    select remain into book_remain from bookinfo where book_id = mybookid;
    select num, is_ban into user_num, user_is_ban from user where user_id = myid;
    select count(*) into is_order from borroworder where book_id = mybookid and user_id = myid;

    set autocommit=0;   # 禁止自动提交，必须commit才能提交

    -- 事务
    insert into borrowinfo(book_id, user_id, borrow_date, return_date, real_date) values(mybookid, myid, curtime(),date_add(curdate(),interval 1 month), null);
    update bookinfo set remain = remain-1 where book_id = mybookid;
    update user set num = num-1 where user_id = myid;
    delete from borroworder where user_id = myid and book_id = mybookid;

    -- 不满足条件，回滚；否则提交事务
    if book_remain = 0 or user_num = 0 or user_is_ban or !is_order then
        set result = false;
        rollback;
    else
        set result = true;
        commit;
    end if;

end//
delimiter ;


-- 还书存储过程
delimiter //    # 定义结束符
create procedure returnbook(in myid int,in mybookid int)
begin

-- 应还
declare user_num int;
-- 用户冻结
declare user_is_ban boolean;

select num, is_ban into user_num, user_is_ban from user where user_id = myid;

-- 事务
update borrowinfo set is_return = true, real_date = curdate() where user_id = myid and book_id = mybookid;
update bookinfo set remain = remain+1 where book_id = mybookid;
update user set num = num + 1 where user_id = myid;

--
if user_is_ban and user_num = 4 then
    update user set is_ban = false where user_id = myid;
end if;

end//
delimiter ;
