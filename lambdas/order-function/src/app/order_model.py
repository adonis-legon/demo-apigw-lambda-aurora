import json

class Order:
    id = 0
    customer_name = ""
    total_amount = 0.0

    def __init__(self, id, customer_name, total_amount):
        self.id = id
        self.customer_name = customer_name
        self.total_amount = total_amount

    def to_json(self):
        return json.dumps(self.__dict__)