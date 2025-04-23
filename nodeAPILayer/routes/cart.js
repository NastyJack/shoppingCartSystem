const express = require("express");
const router = express.Router();
const cartController = require("../controllers/cart");

router.get("/view", cartController.viewCart);
router.post("/calculatePrice", cartController.calculatePrice);
router.post("/addItem", cartController.addItem);
router.delete("/clear", cartController.clearCart);
// router.post("/addItem", cartController.addItem);

module.exports = router;
