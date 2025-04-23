const passport = require("passport");
const JwtStrategy = require("passport-jwt").Strategy;
const ExtractJwt = require("passport-jwt").ExtractJwt;
const config = require("../config/config");

let opts = {};

opts.jwtFromRequest = ExtractJwt.fromAuthHeaderAsBearerToken();
opts.secretOrKey = config.auth.secret;
opts.issuer = config.auth.issuer;
opts.audience = config.auth.audience;
passport.use(
  new JwtStrategy(opts, function (jwt_payload, done) {
    //basic auth to valid User's email from token
    config.allowedusers.has(jwt_payload.email)
      ? done(null, jwt_payload)
      : done(null, false);
  })
);

let isAuthorized = passport.authenticate("jwt", { session: false });

module.exports = isAuthorized;
