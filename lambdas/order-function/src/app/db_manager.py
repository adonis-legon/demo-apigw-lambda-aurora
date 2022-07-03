import os
import psycopg2
import boto3


class DbManager:

    __DB_ENDPOINT = os.getenv('DB_ENDPOINT', '')
    __DB_PORT = os.getenv('DB_PORT', '')
    __DB_USER = os.getenv('DB_USER', '')
    __DB_PASS = os.getenv('DB_PASS', '')
    __DB_NAME = os.getenv('DB_NAME', '')
    __DB_AUTH_IAM = os.getenv('DB_AUTH_IAM', '')
    __DB_REGION = os.getenv('DB_REGION', '')

    db_conn = None

    def is_connected(self):
        return self.db_conn != None and self.db_conn.closed == 0

    def get_db_connection(self):
        if not self.is_connected():
            try:
                db_password = self.__DB_PASS
                ssl_mode = 'disable'
                ssl_root_cert = ''

                if self.__DB_AUTH_IAM and self.__DB_AUTH_IAM.lower() == 'true':
                    db_password = boto3.client('rds').generate_db_auth_token(
                        DBHostname=self.__DB_ENDPOINT, Port=self.__DB_PORT, DBUsername=self.__DB_USER, Region=self.__DB_REGION)

                    ssl_mode = 'require'
                    # TODO: I'm not able to validate RDS's CA-Certificate, so by now just do SSL without Certificate validation
                    # check more in this links:
                    # - https://docs.aws.amazon.com/AmazonRDS/latest/AuroraUserGuide/ssl-certificate-rotation-aurora-postgresql.html
                    # - https://docs.aws.amazon.com/AmazonRDS/latest/UserGuide/PostgreSQL.Concepts.General.SSL.html#PostgreSQL.Concepts.General.SSL.Connecting
                    # - https://www.postgresql.org/docs/current/libpq-connect.html
                    # ssl_root_cert = os.path.join(os.path.dirname(__file__), 'certs', 'rds-ca-root.pem')

                self.db_conn = psycopg2.connect(host=self.__DB_ENDPOINT, port=self.__DB_PORT, database=self.__DB_NAME,
                                                user=self.__DB_USER, password=db_password, sslmode=ssl_mode, sslrootcert=ssl_root_cert)
            except Exception as e:
                raise DbException(
                    f"Error connecting to Postgres Database: {self.__DB_ENDPOINT}. Message: {e.__str__()}")

        return self.db_conn

    def execute_statement_return_id(self, statement):
        self.db_conn = self.get_db_connection()
        db_cursor = self.db_conn.cursor()

        db_cursor.execute(statement)
        return_id = db_cursor.fetchone()[0]

        self.db_conn.commit()
        db_cursor.close()

        return return_id

    def execute_query(self, query):
        self.db_conn = self.get_db_connection()
        db_cursor = self.db_conn.cursor()

        db_cursor.execute(query)
        result = db_cursor.fetchall()

        db_cursor.close()

        return result


class DbException(Exception):
    pass
