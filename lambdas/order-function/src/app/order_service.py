from src.app.order_model import Order
from src.app.order_exceptions import OrderNotFoundException
from src.app.order_dao import save_order, get_by_id

def create_order(order_dto):
    return save_order(Order(0, order_dto["customer_name"], order_dto["total_amount"]))

def find_order_by_id(order_id):
    order = get_by_id(order_id)
    
    if not order:
        raise OrderNotFoundException()
    return order