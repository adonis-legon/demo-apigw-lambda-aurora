import json

from src.app.order_service import create_order, find_order_by_id
from src.app.order_exceptions import *

def handle_event(event, context):
    response = None

    method = event["httpMethod"]
    if method == "POST":
        try:
            order = create_order(json.loads(event["body"]))
            response = build_event_response(201, order.to_json())
        except Exception as e:
            response = build_event_response(500, f"Server error. Message: {e.__str__()}")
    elif method == "GET":
        order_id = event["pathParameters"]["id"] if "pathParameters" in event and "id" in event["pathParameters"] else None
        if not order_id:
            response = build_event_response(400, f"Invalid request, Order Id expected.")
        else:
            try:
                order = find_order_by_id(int(order_id))
                response = build_event_response(200, order.to_json())
            except OrderNotFoundException:
                response = build_event_response(404, f"Order with id {order_id}, not found")
            except Exception as e:
                response = build_event_response(500, f"Server error. Message: {e.__str__()}")
    else:
        response = build_event_response(405, "Method not supported")
        response["statusCode"] = 405
    
    print(f'Response: {response}')
    return response

def build_event_response(http_status, content):
    return {
        "statusCode": http_status,
        "body": content
    }