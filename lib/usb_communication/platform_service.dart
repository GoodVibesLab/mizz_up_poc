import 'package:flutter/services.dart';

class PlatformService {
  static const MethodChannel _channel =
  const MethodChannel('fr.mizzup.healthformankind/channel');

  static Future<String> get connect async {
    final String version = await _channel.invokeMethod('connect');
    return version;
  }

  static Future<String> send(Uint8List data) async {
    final String result = await _channel.invokeMethod('send', {'data': data});
    return result;
  }

}