INSERT INTO public.supplier (name, description)
        VALUES
               ('Amazon', 'Digital content and services'),
               ('Lenovo', 'Computers'),
               ('Dell','Computers');

INSERT INTO public.category (name, department, description)
        VALUES
                ('Tablet', 'Hardware', 'A tablet computer, commonly shortened to tablet, is a thin, flat mobile computer with a touchscreen display.'),
                ('Laptop', 'Hardware', 'A small portable computer that can run on battery power and has the main parts (as keyboard and display screen) combined into a single unit.'),
                ('Desktop Computer', 'Hardware', 'A general-purpose computer equipped with a microprocessor and designed to run especially commercial software (such as a word processor or Internet browser) for an individual user');

INSERT INTO public.product (name, default_price, default_currency, description, category_id, supplier_id)
        VALUES
                ('Amazon Fire', 49.9, 'USD', 'Fantastic price. Large content ecosystem. Good parental controls. Helpful technical support.', 1, 1),
                ('Lenovo IdeaPad Mix 700', 479, 'USD', 'Keyboard cover is included. Fanless Core m5 processor. Full-size USB ports. Adjustable kickstand.', 1, 2),
                ('Amazon Fire HD 8', 89, 'USD', 'Amazon''s latest Fire HD 8 tablet is a great value for media consumption.', 1, 1),
                ('Dell Inspiron 250', 400.01, 'USD', 'Meh',2,3),
                ('Dell Inspiron 330', 249.99, 'USD', 'Meh x Meh',2,3),
                ('Gaming Deck', 350, 'USD', 'Shine bright like a diamond', 3, 3);


                