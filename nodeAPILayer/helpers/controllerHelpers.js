let controllerHelpers = {};

controllerHelpers.handleErrorResponse = function (error, res, status) {
  if (
    error &&
    error.response &&
    error.response.status &&
    error.response.status === 400
  )
    return res.status(400).send({ message: "Bad Request", cart: null });

  return res.status(500).send({
    message: (error && error.message) || "Internal Server error",
    cart: null,
  });
};

module.exports = controllerHelpers;
