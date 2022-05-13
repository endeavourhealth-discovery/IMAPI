import mysql, {Connection, RowDataPacket} from 'mysql2';

export class MysqlService {
  private conn: Connection;

  constructor() {
    this.conn = mysql.createConnection({
      host: process.env.MYSQL_HOST,
      user: process.env.MYSQL_USER,
      password: process.env.MYSQL_PASS,
      database: process.env.MYSQL_DB
    });
  }

  public test(sql: string) {
    return new Promise((resolve, reject) => {
      this.conn.query(sql, (err, result) => {
        if (err) {
          reject(err);
          return;
        } else {
          const rows = <RowDataPacket[]>result;
/*
          rows.forEach(row =>
            console.log(row)
          );*/
          resolve(rows);
        }
      });
    });
  }
}
