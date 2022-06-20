import pytest

from src.test.util import load_test_event

@pytest.fixture(name="create_order_event_ok")
def create_order_event_ok():
    return load_test_event('create-order', 'ok')

@pytest.fixture(name="find_order_by_id_event_ok")
def find_order_by_id_event_ok():
    return load_test_event('find-order-by-id', 'ok')