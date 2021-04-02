DROP TABLE IF EXISTS public.product CASCADE;
CREATE TABLE public.product (
                                   id serial NOT NULL PRIMARY KEY,
                                   name VARCHAR NOT NULL,
                                   description VARCHAR,
                                   default_price real NOT NULL,
                                   default_currency VARCHAR NOT NULL,
                                   category_id INTEGER NOT NULL,
                                   supplier_id INTEGER NOT NULL,

                                   CHECK (default_price > 0)
);

DROP TABLE IF EXISTS public.category CASCADE;
CREATE TABLE public.category (
                               id serial NOT NULL PRIMARY KEY,
                               name VARCHAR NOT NULL,
                               description VARCHAR,
                               department VARCHAR NOT NULL
);

DROP TABLE IF EXISTS public.categories_products;
CREATE TABLE public.categories_products (
                                        category_id integer NOT NULL,
                                        product_id integer NOT NULL
);

DROP TABLE IF EXISTS public.supplier CASCADE;
CREATE TABLE public.supplier (
                                 id serial NOT NULL PRIMARY KEY,
                                 name VARCHAR NOT NULL,
                                 description VARCHAR

);


DROP TABLE IF EXISTS public.suppliers_products;
CREATE TABLE public.suppliers_products (
                                            supplier_id integer NOT NULL,
                                            product_id integer NOT NULL
);


DROP TABLE IF EXISTS public.order;
CREATE TABLE public.order (
                              id serial NOT NULL PRIMARY KEY,
                              name VARCHAR,
                              description VARCHAR,
                              user_id INTEGER NOT NULL
);

DROP TABLE IF EXISTS public.user;
CREATE TABLE public.user (
                             id serial NOT NULL PRIMARY KEY,
                             name VARCHAR NOT NULL,
                             password VARCHAR NOT NULL,
                             email VARCHAR NOT NULL,
                             description VARCHAR
);

DROP TABLE IF EXISTS public.finalorders_users;
CREATE TABLE public.finalorders_users (
                             final_order_id integer NOT NULL,
                             user_id integer NOT NULL
);

DROP TABLE IF EXISTS public.orders_products;
CREATE TABLE public.orders_products (
                                           order_id integer NOT NULL,
                                           product_id integer NOT NULL,
                                           product_quantity integer NOT NULL,

                                           CHECK (product_quantity > 0)
);


DROP TABLE IF EXISTS public.final_order;
CREATE TABLE public.final_order (
                              id serial NOT NULL PRIMARY KEY,
                              name VARCHAR,
                              description VARCHAR,
                              user_id INTEGER NOT NULL,
                              other_details VARCHAR     -- to be updated

);


DROP TABLE IF EXISTS public.finalorders_products;
CREATE TABLE public.finalorders_products (
                                        final_order_id integer NOT NULL,
                                        product_id integer NOT NULL,
                                        product_quantity integer NOT NULL,

                                        CHECK (product_quantity > 0)
);


ALTER TABLE ONLY public.product
    ADD CONSTRAINT fk_category_id FOREIGN KEY (category_id) REFERENCES public.category(id)
    ON DELETE CASCADE;

ALTER TABLE ONLY public.product
    ADD CONSTRAINT fk_supplier_id FOREIGN KEY (supplier_id) REFERENCES public.supplier(id)
    ON DELETE CASCADE;


ALTER TABLE ONLY public.categories_products
    ADD CONSTRAINT pk_category_product_id PRIMARY KEY (category_id, product_id);

ALTER TABLE ONLY public.categories_products
    ADD CONSTRAINT fk_category_id FOREIGN KEY (category_id) REFERENCES public.category(id) ON DELETE CASCADE;

ALTER TABLE ONLY public.categories_products
    ADD CONSTRAINT fk_product_id FOREIGN KEY (product_id) REFERENCES public.product(id) ON DELETE CASCADE;



ALTER TABLE ONLY public.suppliers_products
    ADD CONSTRAINT pk_supplier_product_id PRIMARY KEY (supplier_id, product_id);

ALTER TABLE ONLY public.suppliers_products
    ADD CONSTRAINT fk_supplier_id FOREIGN KEY (supplier_id) REFERENCES public.supplier(id) ON DELETE CASCADE;

ALTER TABLE ONLY public.suppliers_products
    ADD CONSTRAINT fk_product_id FOREIGN KEY (product_id) REFERENCES public.product(id) ON DELETE CASCADE;



ALTER TABLE ONLY public.orders_products
    ADD CONSTRAINT pk_order_product_id PRIMARY KEY (order_id, product_id);

ALTER TABLE ONLY public.orders_products
    ADD CONSTRAINT fk_order_id FOREIGN KEY (order_id) REFERENCES public.order(id) ON DELETE CASCADE;

ALTER TABLE ONLY public.orders_products
    ADD CONSTRAINT fk_product_id FOREIGN KEY (product_id) REFERENCES public.product(id) ON DELETE CASCADE;


ALTER TABLE ONLY public.finalorders_products
    ADD CONSTRAINT pk_finalorder_product_id PRIMARY KEY (final_order_id, product_id);

ALTER TABLE ONLY public.finalorders_products
    ADD CONSTRAINT fk_finalorder_id FOREIGN KEY (final_order_id) REFERENCES public.final_order(id) ON DELETE CASCADE;

ALTER TABLE ONLY public.finalorders_products
    ADD CONSTRAINT fk_product_id FOREIGN KEY (product_id) REFERENCES public.product(id) ON DELETE CASCADE;

ALTER TABLE ONLY public.finalorders_users
    ADD CONSTRAINT pk_finalorder_user_id PRIMARY KEY (final_order_id, user_id);

ALTER TABLE ONLY public.finalorders_users
    ADD CONSTRAINT fk_finalorder_id FOREIGN KEY (final_order_id) REFERENCES public.final_order(id) ON DELETE CASCADE;

ALTER TABLE ONLY public.finalorders_users
    ADD CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES public.user(id) ON DELETE CASCADE;

