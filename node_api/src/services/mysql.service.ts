import mysql, {Connection, RowDataPacket} from 'mysql2';

export class MysqlService {
  private conn: Connection | undefined;

  public execute(sql: string): Promise<RowDataPacket[]> {
    return new Promise((resolve, reject) => {
      this.getConnection().query(sql, (err, result) => {
        if (err) {
          reject(err);
          return;
        } else {
          const rows = <RowDataPacket[]>result;
          resolve(rows);
        }
      });
    });
  }

  private getConnection(): Connection {
    if (!this.conn) {
      this.conn = mysql.createConnection({
        host: process.env.MYSQL_HOST,
        user: process.env.MYSQL_USER,
        password: process.env.MYSQL_PASS,
        database: process.env.MYSQL_DB
      });
    }

    return this.conn;
  }
}
