from src.app.order_model import Order
from src.app.order_exceptions import OrderNotFoundException
from src.app.order_dao import save_order, get_by_id
from src.app.trace_manager import TraceManager

tracer = TraceManager.get_tracer()

def create_order(order_dto):
    new_order = save_order(Order(0, order_dto["customer_name"], order_dto["total_amount"]))
    
    tracer.put_annotation("orderId", new_order.id)
    tracer.put_metadata("resources", new_order)

    return new_order

def find_order_by_id(order_id):
    order = get_by_id(order_id)
    
    if not order:
        raise OrderNotFoundException()
    return order