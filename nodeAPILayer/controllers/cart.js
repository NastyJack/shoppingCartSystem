const axios = require("axios");
const javaBackend = require("../config/config").javaBackend;
const helper = require("../helpers/controllerHelpers");

let cartController = {};

cartController.viewCart = async function (req, res, next) {
  let response, reqUrl, reqVerb, cart;

  try {
    reqUrl = `${javaBackend.baseURL}${javaBackend.routes.viewCart.path}`;
    reqVerb = javaBackend.routes.viewCart.verb;
    response = await axios[reqVerb](reqUrl);

    // throw error if cart data is invalid
    if (
      !(
        response &&
        response.status === 200 &&
        response.data &&
        response.data.status &&
        response.data.status === "success" &&
        response.data.data &&
        Array.isArray(response.data.data)
      )
    )
      throw new Error("Cart data not available");

    //prepare cart data for response
    cart = response.data.data;

    if (cart.length === 0)
      return res.status(200).send({ message: "Cart is empty", cart: cart });

    return res
      .status(200)
      .send({ message: "Items available in cart", cart: cart });
  } catch (error) {
    // console.log("Error at cartController.getCart", error);

    return helper.handleErrorResponse(error, res);
  }
};

cartController.calculatePrice = async function (req, res, next) {
  let response, reqUrl, reqVerb, price;

  try {
    reqUrl = `${javaBackend.baseURL}${javaBackend.routes.calculatePrice.path}`;
    reqVerb = javaBackend.routes.calculatePrice.verb;
    response = await axios[reqVerb](reqUrl);

    // throw error if invalid price is returned
    if (
      !(
        response &&
        response.status === 200 &&
        response.data &&
        response.data.status &&
        response.data.status === "success" &&
        response.data.hasOwnProperty("data") &&
        typeof response.data.data === "number"
      )
    )
      throw new Error("Data invalid for calculated price");

    price = response.data.data;

    return res
      .status(200)
      .send({ message: "Price Calculation Successful", price: `${price}p` });
  } catch (error) {
    // console.log("Error at cartController.calculatePrice", error);

    return helper.handleErrorResponse(error, res);
  }
};

cartController.clearCart = async function (req, res, next) {
  let response, reqUrl, reqVerb;

  try {
    reqUrl = `${javaBackend.baseURL}${javaBackend.routes.clearCart.path}`;
    reqVerb = javaBackend.routes.clearCart.verb;
    response = await axios[reqVerb](reqUrl);

    // throw error if invalid price is returned
    if (
      !(
        response &&
        response.status === 200 &&
        response.data &&
        response.data.status &&
        response.data.status === "success"
      )
    )
      throw new Error("Failed to clear cart.");

    return res
      .status(200)
      .send({ message: "Cart has been cleared.", cart: [] });
  } catch (error) {
    // console.log("Error at cartController.clearCart", error);

    return helper.handleErrorResponse(error, res);
  }
};

cartController.addItem = async function (req, res, next) {
  let response, reqUrl, reqVerb, reqBody;

  try {
    // ensure request body has item property
    if (!(req.body && req.body.item && typeof req.body.item === "string"))
      return res.status(400).send({
        message: "Invalid request body. Item is required.",
        cart: null,
      });

    // reject request if item is not valid
    if (!javaBackend.routes.addItem.validateItem.has(req.body.item))
      return res.status(400).send({
        message: "Invalid item. Item cannot be added to cart.",
        cart: null,
      });

    reqBody = { name: req.body.item };
    reqUrl = `${javaBackend.baseURL}${javaBackend.routes.addItem.path}`;
    reqVerb = javaBackend.routes.addItem.verb;
    response = await axios[reqVerb](reqUrl, reqBody);

    // throw error if failed to add item to cart
    if (
      !(
        response &&
        response.status === 200 &&
        response.data &&
        response.data.status &&
        response.data.status === "success"
      )
    )
      throw new Error("Failed to add item to cart.");

    cart = response.data.data;

    return res.status(200).send({ message: "Item added to cart.", cart: cart });
  } catch (error) {
    console.log("Error at cartController.addItem", error);

    return helper.handleErrorResponse(error, res);
  }
};
module.exports = cartController;
