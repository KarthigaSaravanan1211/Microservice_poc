CREATE OR REPLACE PROCEDURE debit_account(
    p_user_id BIGINT,
    p_amount NUMERIC
)
LANGUAGE plpgsql
AS $$
DECLARE
    current_balance NUMERIC;
BEGIN
    -- Lock row to prevent race condition
    SELECT balance INTO current_balance
    FROM accounts
    WHERE user_id = p_user_id
    FOR UPDATE;

    IF current_balance IS NULL THEN
        RAISE EXCEPTION 'Account not found';
    END IF;

    IF current_balance < p_amount THEN
        RAISE EXCEPTION 'Insufficient balance';
    END IF;

    UPDATE accounts
    SET balance = balance - p_amount
    WHERE user_id = p_user_id;
END;
$$;
