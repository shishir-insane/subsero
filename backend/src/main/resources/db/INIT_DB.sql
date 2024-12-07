CREATE SCHEMA `ghostdb` ;
CREATE USER 'subsero_owner' IDENTIFIED BY 'P@55word_';
GRANT ALL PRIVILEGES on ghostdb.* TO 'subsero_owner' WITH GRANT OPTION;

-- Subscription Tracker Database Schema
-- MySQL DDL Scripts

-- Create Users Table
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT chk_email_valid CHECK (email REGEXP '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$')
) COMMENT 'Stores user account information';

-- Create Subscriptions Table
CREATE TABLE subscriptions (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    name VARCHAR(100) NOT NULL,
    cost DECIMAL(10, 2) NOT NULL,
    renewal_date DATE NOT NULL,
    category VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT chk_cost_positive CHECK (cost >= 0)
) COMMENT 'Tracks individual subscription details for each user';

-- Create Notifications Table
CREATE TABLE notifications (
    id INT AUTO_INCREMENT PRIMARY KEY,
    subscription_id INT NOT NULL,
    method ENUM('email', 'sms') NOT NULL,
    reminder_date DATETIME NOT NULL,
    status ENUM('pending', 'sent', 'failed') DEFAULT 'pending',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (subscription_id) REFERENCES subscriptions(id) ON DELETE CASCADE
) COMMENT 'Manages reminders and alerts for subscription renewals';

-- Create Analytics Table
CREATE TABLE analytics (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL UNIQUE,
    total_monthly_cost DECIMAL(10, 2) NOT NULL DEFAULT 0,
    total_annual_cost DECIMAL(10, 2) NOT NULL DEFAULT 0,
    most_expensive_subscription VARCHAR(100),
    least_used_subscription VARCHAR(100),
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT chk_monthly_cost_non_negative CHECK (total_monthly_cost >= 0),
    CONSTRAINT chk_annual_cost_non_negative CHECK (total_annual_cost >= 0)
) COMMENT 'Provides aggregated insights on user subscription spending';

-- Create Payments Table
CREATE TABLE payments (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    plan_name VARCHAR(50) NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    payment_date TIMESTAMP NOT NULL,
    status ENUM('successful', 'failed', 'pending') NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT chk_amount_positive CHECK (amount > 0)
) COMMENT 'Tracks premium subscription plans and transactions';

-- Indexes for Performance Optimization
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_subscriptions_user_id ON subscriptions(user_id);
CREATE INDEX idx_notifications_subscription_id ON notifications(subscription_id);
CREATE INDEX idx_payments_user_id ON payments(user_id);
CREATE INDEX idx_payments_status ON payments(status);
CREATE INDEX idx_subscriptions_renewal_date ON subscriptions(renewal_date);

-- Optional: Triggers for Additional Data Integrity

-- Trigger to update analytics when a subscription is added or modified
DELIMITER $$
CREATE TRIGGER update_user_analytics_after_subscription 
AFTER INSERT ON subscriptions
FOR EACH ROW
BEGIN
    -- Update total monthly and annual costs
    UPDATE analytics a
    SET 
        a.total_monthly_cost = (
            SELECT SUM(cost) 
            FROM subscriptions 
            WHERE user_id = NEW.user_id
        ),
        a.total_annual_cost = (
            SELECT SUM(cost * 12) 
            FROM subscriptions 
            WHERE user_id = NEW.user_id
        ),
        a.most_expensive_subscription = (
            SELECT name 
            FROM subscriptions 
            WHERE user_id = NEW.user_id 
            ORDER BY cost DESC 
            LIMIT 1
        )
    WHERE a.user_id = NEW.user_id;
END$$
DELIMITER ;

-- Notes:
-- 1. ON DELETE CASCADE ensures that related records are automatically deleted
-- 2. UNIQUE constraints and indexes are added for performance and data integrity
-- 3. CHECK constraints validate data before insertion
-- 4. Trigger demonstrates how to keep analytics table updated automatically