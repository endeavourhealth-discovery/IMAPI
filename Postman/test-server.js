require("dotenv").config(); // ✅ Load .env file

const express = require("express");
const mysql = require("mysql2");
const bodyParser = require("body-parser");

const app = express();
app.use(bodyParser.json());

// ✅ Configure MySQL connection using .env values
const db = mysql.createConnection({
  host: process.env.MYSQL_HOST,
  user: process.env.MYSQL_USER,
  password: process.env.MYSQL_PASSWORD,
  database: process.env.MYSQL_DATABASE,
});

// ✅ Connect to MySQL
db.connect((err) => {
  if (err) {
    console.error("❌ MySQL connection failed:", err);
  } else {
    console.log("✅ Connected to MySQL");
  }
});

// ✅ Example endpoint: Run arbitrary SQL query (⚠️ only for testing, not safe for prod!)
app.post("/query", (req, res) => {
  const { sql } = req.body;

  if (!sql) {
    return res.status(400).json({ error: "Missing SQL query in request body" });
  }

  const now = new Date();

  const year = now.getFullYear();
  const month = String(now.getMonth() + 1).padStart(2, "0"); // months are 0-based
  const day = String(now.getDate()).padStart(2, "0");
  const formatted = `${year}/${month}/${day}`;

  let sqlResolved = sql.replaceAll("$searchDate", `'${formatted}'`);
  sqlResolved = sqlResolved.replaceAll("$achievementDate", `'${formatted}'`);

  db.query(sqlResolved, (err, results) => {
    if (err) {
      return res.status(500).json({ error: err.message });
    }
    res.json({ results });
  });
});

// ✅ Start server
const PORT = process.env.PORT || 3000;
app.listen(PORT, () => {
  console.log(`🚀 Server running at http://localhost:${PORT}`);
});
