import json

import pytest
from src.app.order_handler import handle_event
from src.app.order_service import *
from src.app.order_model import Order
from src.test.fixtures import create_order_event_ok, find_order_by_id_event_ok

@pytest.mark.skip(reason="this is an integration test")
def test_create_order_event(create_order_event_ok):
    expected_order = Order(0, 'Demo User', 1234.5)

    response = handle_event(create_order_event_ok, None)
    expected_order.id = json.loads(response["body"])["id"]

    assert response["statusCode"] == 201 and response["body"] == expected_order.to_json()

@pytest.mark.skip(reason="this is an integration test")
def test_find_orde_by_id_event(find_order_by_id_event_ok):
    expected_order = Order(1, 'Demo User', 1234.5)

    response = handle_event(find_order_by_id_event_ok, None)
    assert response["statusCode"] == 200 and response["body"] == expected_order.to_json()