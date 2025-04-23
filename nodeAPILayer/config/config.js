let config = {
  PORT: 9090,
  javaBackend: {
    baseURL: "http://localhost:8080/",
    routes: {
      viewCart: { verb: "get", path: "cart/view" },
      addItem: {
        verb: "post",
        path: "cart/addItem",
        validateItem: new Set(["Apple", "Banana", "Melon", "Lime"]),
      },
      calculatePrice: { verb: "post", path: "cart/calculatePrice" },
      clearCart: { verb: "delete", path: "cart/clear" },
    },
  },
  allowedusers: new Set([
    "userA@gmail.com",
    "userB@gmail.com",
    // "userC@gmail.com", //marks as invalid token
  ]),
  auth: {
    secret: "secretXYZ",
    issuer: "somerandomissuer.com",
    audience: "somerandomwebsite.net",
  },
};

module.exports = config;
