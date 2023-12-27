// ignore_for_file: unnecessary_null_comparison

import 'dart:async';
import 'dart:typed_data';

import 'package:flutter/material.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:mizz_up_poc/usb_communication/platform_service.dart';
import 'package:usb_serial/transaction.dart';
import 'package:usb_serial/usb_serial.dart';

void main() => runApp(MyApp());

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  UsbPort? _port;
  String eventText = '';
  String _print = '';
  String _status = "Idle";
  String _event1 = '';
  List<Widget> _ports = [];
  List<Widget> _serialData = [];

  StreamSubscription<String>? _subscription;
  Transaction<String>? _transaction;
  UsbDevice? _device;

  TextEditingController _textController = TextEditingController();

  Future<bool> _connectToTest(device) async {
    _serialData.clear();

    if (_subscription != null) {
      _subscription?.cancel();
      _subscription = null;
    }

    if (_transaction != null) {

      _transaction = null;
    }


    if (_port != null) {
      _port?.close();
      _port = null;
    }


    if (device == null) {
      _device = null;
      setState(() {
        _status = "Disconnected";
      });
      return true;
    }


    try {
      _port = await device[0].create();
    } catch (e) {
      setState(() {
        _print = "_port: $e";
      });
    }

    // _port = await device[0].create();

    /*
    if (await (_port!.open()) != true) {
      setState(() {
        _status = "Failed to open port";
      });
      return false;
    }
    _device = device[0];
    */

    try {
      await _port?.open();
    } catch (e) {
      setState(() {
        _print = "Port open: $e";
      });
    }

    try {
      await _port?.setDTR(true);
    } catch (e) {
      setState(() {
        _print = "setDTR: $e";
      });
    }

    try {
      await _port?.setRTS(true);
    } catch (e) {
      setState(() {
        _print = "setRTS: $e";
      });
    }

    try {
      await _port?.setPortParameters(
          115200, UsbPort.DATABITS_8, UsbPort.STOPBITS_1, UsbPort.PARITY_NONE);
    } catch (e) {
      setState(() {
        _print = "setPortParameters: $e";
      });
    }

    try {
      _port?.inputStream?.listen((Uint8List event1) {
        print(event1);
        _event1 = event1.toString();
        _port?.close();
      });
    } catch (e) {
      setState(() {
        _print = "port inputStream: $e AND $_event1";
      });
    }
/*
    try {
      await _port?.write(Uint8List.fromList([0x10, 0x00]));
    } catch (e) {
      setState(() {
        _print = "port write: $e";
      });
    }
*/


    try {
// _transaction = Transaction.stringTerminated(_port!.inputStream!, Uint8List.fromList([13, 10]));
      var test = await _port?.write(Uint8List.fromList([0x10, 0x00]));




    } catch (e) {
      setState(() {
        _print = "transaction: $e";
      });
    }


    setState(() {
      _status = "Connected";
    });
    return true;
  }



  Future<bool> _connectTo(List<UsbDevice>? device) async {
    _serialData.clear();

    if (_subscription != null) {
      _subscription?.cancel();
      _subscription = null;
    }

    if (_transaction != null) {

      _transaction = null;
    }


    if (_port != null) {
      _port?.close();
      _port = null;
    }


    setState(() {
      _print = "Step 1";
    });
    if (device == null) {
      _device = null;
      setState(() {
        _status = "Disconnected";
      });
      return true;
    }

    try {
      _port = await device[0].create();
    } catch (e) {
      setState(() {
        _print = "_port: $e";
      });
    }

    // _port = await device[0].create();

    /*
    if (await (_port!.open()) != true) {
      setState(() {
        _status = "Failed to open port";
      });
      return false;
    }
    _device = device[0];
    */

    try {
      await _port?.open();
    } catch (e) {
      setState(() {
        _print = "Port open: $e";
      });
    }

    try {
      await _port?.setDTR(true);
    } catch (e) {
      setState(() {
        _print = "setDTR: $e";
      });
    }

    try {
      await _port?.setRTS(true);
    } catch (e) {
      setState(() {
        _print = "setRTS: $e";
      });
    }

    try {
      await _port?.setPortParameters(
          115200, UsbPort.DATABITS_8, UsbPort.STOPBITS_1, UsbPort.PARITY_NONE);
    } catch (e) {
      setState(() {
        _print = "setPortParameters: $e";
      });
    }

    try {
      _port?.inputStream?.listen((Uint8List event1) {
        print(event1);
        _event1 = event1.toString();
        _port?.close();
      });
    } catch (e) {
      setState(() {
        _print = "port inputStream: $e AND $_event1";
      });
    }
/*
    try {
      await _port?.write(Uint8List.fromList([0x10, 0x00]));
    } catch (e) {
      setState(() {
        _print = "port write: $e";
      });
    }
*/


    try {
      _transaction = Transaction.stringTerminated(_port!.inputStream!, Uint8List.fromList([13, 10]));

      _subscription = _transaction?.stream.listen((String line) {
        setState(() {
          _serialData.add(Text(line));
          if (_serialData.length > 20) {
            _serialData.removeAt(0);
          }
        });
      });
    } catch (e) {
      setState(() {
        _print = "transaction: $e AND $_serialData";
      });
    }


    setState(() {
      _status = "Connected";
    });
    return true;
  }




  Future _getPorts(UsbDevice? event) async {
    _ports = [];
    List<UsbDevice>? listDevices = [];
    if (event == null) {
      _connectTo(null);
    }

    _ports.add(ListTile(
        leading: const Icon(Icons.usb),
        title: Text(event?.productName ?? ''),
        subtitle: Text(event?.deviceId.toString() ?? ''),
        trailing: ElevatedButton(
          child: Text(_device == event ? "Disconnect" : "Connect"),
          onPressed: () async {
            listDevices.add(event!);
            await _connectTo(listDevices).then((res) async {
              await _getPorts(event);
            });
          },
        )));

    setState(() {
      print(_ports);
      portDataBits = event?.port?.dataBits.toString() ?? "";
      portBaudRate = event?.port?.baudRate.toString() ?? "";
    });
  }



  String deviceId = '';
  String deviceName = '';
  String interfaceCount = '';
  String pid = '';
  String portDataBits = '';
  String portBaudRate = '';
  String productName = '';
  String serial = '';
  String vid = '';

  Future _getPortsTest() async {
    _ports = [];
    try{
      List<UsbDevice> devices = await UsbSerial.listDevices();

      if (!devices.contains(_device)) {
        _connectToTest(null);
      }


      for (var device in devices) {
        _ports.add(ListTile(
            leading: const Icon(Icons.usb),
            title: Text(device.productName ?? ""),
            //  subtitle: Text(device.),
            trailing: ElevatedButton(
              child: Text(_device == device ? "Disconnect" : "Connect"),
              onPressed: () async {
                await _connectToTest(device).then((res) async {
                  await  _getPortsTest();
                });
              },
            )));
      }
    }
    catch (e){
      setState(() {
        _print = "GETPORTS TEST: $e";
      });
    }


    setState(() {
    });
  }

  @override
  void initState() {
    super.initState();

    _getPortsTest();

    UsbSerial.usbEventStream!.listen((UsbEvent event) {
      eventText = event.event!;
      deviceId = event.device!.deviceId.toString();
      deviceName = event.device!.deviceName;
      interfaceCount = event.device!.interfaceCount.toString();
      pid = event.device!.pid.toString();
      portDataBits = event.device!.port?.dataBits.toString() ?? "";
      serial = event.device!.serial ?? "";
      vid = event.device!.vid.toString();
      _getPorts(event.device);
    });

  }

/*
  @override
  void dispose() {
    super.dispose();
    _connectTo(null);
  }
*/

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
        home: Scaffold(
          appBar: AppBar(
            title: const Text('Communication with Dongle'), actions: [IconButton(onPressed: () => _getPortsTest(), icon: const Icon(Icons.refresh, color: Colors.white,))],
          ),
          body: Center(
            child: Column(children: <Widget>[
              Text(
                  _ports.isNotEmpty ? "Device is connected" : "No device available",
                  style: Theme.of(context).textTheme.titleLarge),
              ..._ports,
              Text('Status: $_status\n'),
              Text('PRINT: $_print\n'),

              Text('deviceID: $deviceId'),
              Text('deviceName: $deviceName'),
              Text('interfaceCount: $interfaceCount'),
              Text('pid: $pid'),
              Text('portDataBits: $portDataBits'),
              Text('portBaudRate: $portBaudRate'),
              Text('productName: $productName'),
              Text('serial: $serial'),
              Text('vid: $vid'),

              _connectButton,

              ListTile(
                title: TextField(
                  controller: _textController,
                  decoration: const InputDecoration(
                    border: OutlineInputBorder(),
                    labelText: 'Text To Send',
                  ),
                ),
                trailing: ElevatedButton(
                  onPressed: () async {
                    PlatformService.send(_textController.text);
                    _textController.text = "";
                  },
                  child: const Text("Send"),
                ),
              ),
              Text("Result Data", style: Theme.of(context).textTheme.titleLarge),
              ..._serialData,

            ]),
          ),
        ));
  }

  Widget get _connectButton {
    return ElevatedButton(
      onPressed: () async {
        try {
          String connect = await PlatformService.connect;
          _showToast(context, connect);
        } on Exception catch (e) {
          _showToast(context, e.toString());
        }
      },
      child: const Text("Connect"),
    );
  }

  void _showToast(BuildContext context, String text) {
    debugPrint(text);
    Fluttertoast.showToast(
        msg: text,
        toastLength: Toast.LENGTH_SHORT,
        gravity: ToastGravity.CENTER,
        fontSize: 16.0
    );
  }
}
