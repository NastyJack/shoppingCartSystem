const express = require("express");
const bodyParser = require("body-parser");
const app = express();
const cors = require("cors");

const isAuthorized = require("./middlewares/authorization");
const cart = require("./routes/cart");
const logger = require("./middlewares/logger");

// Path for log file

app.use(cors());

// Body Parser
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true }));

// Api routes
app.use("/cart", isAuthorized, cart);

app.get("/", (req, res, next) => {
  console.log("Node.JS API Server");
  res.send("Node.JS API Server");
});

// Unkown Routes
app.use(/(.*)/, (req, res, next) => {
  res.status(404).send("Route not found");
});

let PORT = process.env.PORT || 9090;

app.listen(PORT, () => console.log(`Server is running on PORT ${PORT}`));
