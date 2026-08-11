CREATE TABLE menu_items (
    id BIGINT NOT NULL AUTO_INCREMENT,

    restaurant_id BIGINT NOT NULL,
    category_id BIGINT NOT NULL,

    name VARCHAR(150) NOT NULL,
    description VARCHAR(500),

    price DECIMAL(10, 2) NOT NULL,

    image_url VARCHAR(500),

    available BOOLEAN NOT NULL DEFAULT TRUE,

    preparation_time_minutes INT NOT NULL DEFAULT 15,

    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NOT NULL,

    CONSTRAINT pk_menu_items
        PRIMARY KEY (id),

    CONSTRAINT fk_menu_items_restaurant
        FOREIGN KEY (restaurant_id)
        REFERENCES restaurants(id),

    CONSTRAINT fk_menu_items_category
        FOREIGN KEY (category_id)
        REFERENCES menu_categories(id),

    CONSTRAINT uk_menu_items_category_name
        UNIQUE (category_id, name),

    CONSTRAINT chk_menu_items_price
        CHECK (price >= 0),

    CONSTRAINT chk_menu_items_preparation_time
        CHECK (preparation_time_minutes >= 0)
);