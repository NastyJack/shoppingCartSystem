const axios = require("axios");
const fs = require("fs");
const path = require("path");
const { v4: uuidv4 } = require("uuid");

const logFilePath = path.join(__dirname, "..", "logs", "api_requests.log");

//delete log file on start
if (fs.existsSync(logFilePath)) fs.unlinkSync(logFilePath);

function logToFile(data) {
  try {
    const dir = path.dirname(logFilePath);
    if (!fs.existsSync(dir)) {
      fs.mkdirSync(dir, { recursive: true });
    }
    fs.appendFileSync(logFilePath, JSON.stringify(data) + "\n");
  } catch (err) {
    console.error("Failed to write log:", err);
  }
}

// 📤 Request Interceptor
axios.interceptors.request.use(
  (config) => {
    const requestId = uuidv4();
    config.metadata = {
      requestId,
      startTime: new Date(),
      requestInfo: {
        id: requestId,
        timestamp: new Date().toISOString(),
        method: config.method,
        url: config.url,
        headers: config.headers,
        data: config.data,
      },
    };
    return config;
  },
  (error) => {
    // Error before request was sent
    const logEntry = {
      request: {
        id: uuidv4(),
        timestamp: new Date().toISOString(),
        method: "unknown",
        url: "unknown",
        headers: {},
        data: null,
      },
      response: {
        timestamp: new Date().toISOString(),
        status: "N/A",
        error: "Request setup failed: " + error.message,
      },
    };
    logToFile(logEntry);
    return Promise.reject(error);
  }
);

// 📥 Response Interceptor
axios.interceptors.response.use(
  (response) => {
    const endTime = new Date();
    const duration = endTime - response.config.metadata.startTime;

    const logEntry = {
      request: response.config.metadata.requestInfo,
      response: {
        timestamp: endTime.toISOString(),
        status: response.status,
        durationMs: duration,
        data: response.data,
      },
    };

    logToFile(logEntry);
    return response;
  },
  (error) => {
    const config = error.config || {};
    const endTime = new Date();

    const requestInfo = config.metadata?.requestInfo || {
      id: uuidv4(),
      timestamp: new Date().toISOString(),
      method: config.method || "unknown",
      url: config.url || "unknown",
      headers: config.headers || {},
      data: config.data || null,
    };

    const logEntry = {
      request: requestInfo,
      response: {
        timestamp: endTime.toISOString(),
        status: error.response?.status || "N/A",
        durationMs: config.metadata
          ? endTime - config.metadata.startTime
          : null,
        error: error.message,
        responseData: error.response?.data || null,
      },
    };

    logToFile(logEntry);
    return Promise.reject(error);
  }
);
