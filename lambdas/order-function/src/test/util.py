import json
import os


def load_test_event(events_folder, event_name):
    event_path = os.path.join(os.path.dirname(__file__), events_folder, 'events', f'{event_name}.json')
    with open(event_path) as json_file:
        return json.loads(json_file.read())