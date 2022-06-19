from src.app.order_model import Order

# TEMP
orders = []

def save_order(order):
    order.id = len(orders)+1
    orders.append(order)

    return order

def get_by_id(order_id):
    order = list(filter(lambda x: x.id == order_id, orders))
    return order[0] if len(order) > 0 else None