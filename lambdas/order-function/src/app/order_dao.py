from src.app.order_model import Order
from src.app.db_manager import DbManager

db_manager = DbManager()

def save_order(order):
    insert_st = f'''insert into customer_order (customer_name, total_amount) 
    values('{order.customer_name}', {order.total_amount}) 
    returning id'''.replace('\n', '');

    order.id = db_manager.execute_statement_return_id(insert_st)
    return order

def get_by_id(order_id):
    select_st = f"select * from customer_order where id = {order_id}";
    
    orders = db_manager.execute_query(select_st)
    if len(orders) > 0:
        order = orders[0]
        return Order(order[0], order[1], float(str(order[2])))

    return None