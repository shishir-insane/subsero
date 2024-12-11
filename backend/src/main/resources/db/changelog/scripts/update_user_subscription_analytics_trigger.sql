CREATE TRIGGER update_user_subscription_analytics 
AFTER INSERT ON subscriptions
FOR EACH ROW
BEGIN
    -- Upsert analytics data
    INSERT INTO user_subscription_analytics (
        user_id, 
        total_monthly_cost, 
        total_annual_cost, 
        most_expensive_subscription,
        total_subscriptions,
        active_subscriptions
    ) VALUES (
        NEW.user_id,
        (SELECT SUM(cost) FROM subscriptions WHERE user_id = NEW.user_id),
        (SELECT SUM(cost * 12) FROM subscriptions WHERE user_id = NEW.user_id),
        (SELECT name FROM subscriptions WHERE user_id = NEW.user_id ORDER BY cost DESC LIMIT 1),
        (SELECT COUNT(*) FROM subscriptions WHERE user_id = NEW.user_id),
        (SELECT COUNT(*) FROM subscriptions WHERE user_id = NEW.user_id AND is_active = TRUE)
    ) ON DUPLICATE KEY UPDATE 
        total_monthly_cost = VALUES(total_monthly_cost),
        total_annual_cost = VALUES(total_annual_cost),
        most_expensive_subscription = VALUES(most_expensive_subscription),
        total_subscriptions = VALUES(total_subscriptions),
        active_subscriptions = VALUES(active_subscriptions);
END;