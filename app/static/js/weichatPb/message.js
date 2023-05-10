module.exports = {
    "options": {
        "java_package": "com.walabot.home.companion.ble"
    },
    "nested": {
        "ToDeviceMessageType": {
            "values": {
                "CONNECT_WIFI": 0,
                "CLOUD_CONNECT": 1,
                "PAIR_TO_PHONE": 2,
                "PAIR_TO_PHONE_COMPLETE": 3,
                "GET_OTA_VERSION": 4,
                "PERFORM_OTA": 5,
                "DO_REBOOT_FACTORY": 6,
                "DO_REBOOT_OPERATIONAL": 7,
                "DO_WIFI_SCAN": 8,
                "IM_HERE": 9
            }
        },
        "ToAppMessageType": {
            "values": {
                "CONNECT_WIFI_RESULT": 0,
                "CLOUD_CONNECT_RESULT": 1,
                "PAIR_TO_PHONE_RESULT": 2,
                "CHECK_OTA_RESULT": 3,
                "DO_REBOOT_RESULT": 4,
                "PERFORM_OTA_RESULT": 5,
                "AP_SCAN_RESULT": 6,
                "GENERIC_RESULT": 99
            }
        },
        "ToAppResultType": {
            "values": {
                "OK": 0,
                "INVALID_STATE": 1,
                "ESP_ERROR": 2,
                "WIFI_WRONG_CREDENTIALS": 3,
                "AUTHORIZED": 4,
                "SERVER_NOT_FOUND": 5,
                "NO_OTA_FOUND": 6,
                "NETWORK_IS_LOCAL_ONLY": 10,
                "COULD_NOT_UPDATE_TIME": 11,
                "UNAUTHORIZED": 12,
                "PAIR_CODE_FAIL": 13,
                "PAIR_CODE_DELETE_FAIL": 14
            }
        },
        "CLOUD_TYPE": {
            "values": {
                "CUSTOM": 0,
                "GOOLE_CLOUD": 1,
                "GOOLE_IOT": 2,
                "ALI_BABA": 3
            }
        },
        "WifiCred": {
            "fields": {
                "ssid": {
                    "type": "string",
                    "id": 1
                },
                "pass": {
                    "type": "string",
                    "id": 2
                }
            }
        },
        "WifiCredResult": {
            "fields": {
                "ip": {
                    "type": "bytes",
                    "id": 1
                },
                "mac": {
                    "type": "bytes",
                    "id": 2
                }
            }
        },
        "CloudDetails": {
            "fields": {
                "projectId": {
                    "type": "string",
                    "id": 1
                },
                "httpUrl": {
                    "type": "string",
                    "id": 2
                },
                "mqttUri": {
                    "type": "string",
                    "id": 3
                },
                "mqttPort": {
                    "type": "int32",
                    "id": 4
                },
                "ntpUrl": {
                    "type": "string",
                    "id": 5
                },
                "mqttClientId": {
                    "type": "string",
                    "id": 6
                },
                "mqttUsername": {
                    "type": "string",
                    "id": 7
                },
                "mqttPassword": {
                    "type": "string",
                    "id": 8
                },
                "cloudType": {
                    "type": "CLOUD_TYPE",
                    "id": 9
                },
                "cloudRegion": {
                    "type": "string",
                    "id": 10
                },
                "cloudRegistry": {
                    "type": "string",
                    "id": 11
                }
            }
        },
        // "CloudDetails": {
        //     "fields": {
        //         "projectId": {
        //             "type": "string",
        //             "id": 1
        //         },
        //         "httpUrl": {
        //             "type": "string",
        //             "id": 2
        //         },
        //         "ntpUrl": {
        //             "type": "string",
        //             "id": 3
        //         },
        //         "cloudType": {
        //             "type": "CLOUD_TYPE",
        //             "id": 4
        //         }
        //     }
        // },
        "Pair": {
            "fields": {
                "uid": {
                    "type": "string",
                    "id": 1
                }
            }
        },
        "AccessPoint": {
            "fields": {
                "ssid": {
                    "type": "string",
                    "id": 1
                },
                "bssid": {
                    "type": "string",
                    "id": 2
                },
                "rssi": {
                    "type": "int32",
                    "id": 3
                },
                "channel": {
                    "type": "int32",
                    "id": 4
                }
            }
        },
        "ScanResult": {
            "fields": {
                "accessPoint": {
                    "rule": "repeated",
                    "type": "AccessPoint",
                    "id": 1
                }
            }
        },
        "PairResult": {
            "fields": {
                "code": {
                    "type": "string",
                    "id": 1
                }
            }
        },
        "PairingComplete": {
            "fields": {
                "uid": {
                    "type": "string",
                    "id": 1
                },
                "code": {
                    "type": "string",
                    "id": 2
                }
            }
        },
        "OTAResult": {
            "fields": {
                "hasNewVersion": {
                    "type": "bool",
                    "id": 1
                },
                "currentVersionCode": {
                    "type": "int32",
                    "id": 2
                },
                "updateVersionCode": {
                    "type": "int32",
                    "id": 3
                }
            }
        },
        "OTAPerform": {
            "fields": {
                "updateVersionCode": {
                    "type": "int32",
                    "id": 3
                }
            }
        },
        "ToDeviceMessage": {
            "fields": {
                "type": {
                    "type": "ToDeviceMessageType",
                    "id": 1
                },
                "payload": {
                    "type": "bytes",
                    "id": 2
                }
            }
        },
        "ToAppMessage": {
            "fields": {
                "type": {
                    "type": "ToAppMessageType",
                    "id": 1
                },
                "result": {
                    "type": "ToAppResultType",
                    "id": 2
                },
                "payload": {
                    "type": "bytes",
                    "id": 3
                }
            }
        },
        "ToAppResultType2": {
            "values": {
                "SUCCESS": 0,
                "INVALID_REQUEST": 1,
                "BLE_SIZE_ERROR": 2,
                "WIFI_CREDENTIALS_ERROR": 11,
                "LOW_WIFI_SIGNAL": 12,
                "WIFI_LOCAL_ONLY": 13,
                "WIFI_SCAN_ERROR_TIMEOUT": 14,
                "NTP_SERVER_ERROR": 21,
                "READING_PAIR_CODE_FAILED": 31
            }
        },
        "WifiCred2": {
            "fields": {
                "ssid": {
                    "type": "string",
                    "id": 1
                },
                "pass": {
                    "type": "string",
                    "id": 2
                },
                "minRssi": {
                    "type": "int32",
                    "id": 3
                }
            }
        },
        "WifiCredResult3": {
            "fields": {
                "type": {
                    "type": "ToAppMessageType",
                    "id": 1
                },
                "result": {
                    "type": "ToAppResultType2",
                    "id": 2
                },
                "espError": {
                    "type": "int32",
                    "id": 3
                },
                "mac": {
                    "type": "int64",
                    "id": 4
                }
            }
        },
        "CloudDetails2": {
            "fields": {
                "projectId": {
                    "type": "string",
                    "id": 1
                },
                "httpUrl": {
                    "type": "string",
                    "id": 2
                },
                "ntpUrl": {
                    "type": "string",
                    "id": 5
                }
            }
        },
        "ToAppMessage2": {
            "oneofs": {
                "payload": {
                    "oneof": [
                        "pair",
                        "scan"
                    ]
                }
            },
            "fields": {
                "type": {
                    "type": "ToAppMessageType",
                    "id": 1
                },
                "result": {
                    "type": "ToAppResultType2",
                    "id": 2
                },
                "espError": {
                    "type": "int32",
                    "id": 3
                },
                "pair": {
                    "type": "PairResult",
                    "id": 4
                },
                "scan": {
                    "type": "ScanResult",
                    "id": 5
                }
            }
        }
    }
}