from aws_lambda_powertools import Tracer

class TraceManager:
    tracer = None

    @staticmethod
    def get_tracer():
        if not TraceManager.tracer:
            TraceManager.tracer = Tracer()

        return TraceManager.tracer