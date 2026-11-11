import AppKit

let arguments = CommandLine.arguments
guard arguments.count >= 4, let size = Int(arguments[3]) else {
    fatalError("usage: render_png.swift <input.svg> <output.png> <size> [--opaque]")
}
let opaque = arguments.contains("--opaque")

guard let svg = NSImage(contentsOf: URL(fileURLWithPath: arguments[1])) else {
    fatalError("cannot load \(arguments[1])")
}

let alphaInfo: CGImageAlphaInfo = opaque ? .noneSkipLast : .premultipliedLast
let context = CGContext(
    data: nil,
    width: size,
    height: size,
    bitsPerComponent: 8,
    bytesPerRow: 0,
    space: CGColorSpace(name: CGColorSpace.sRGB)!,
    bitmapInfo: alphaInfo.rawValue
)!

NSGraphicsContext.current = NSGraphicsContext(cgContext: context, flipped: false)
svg.draw(in: NSRect(x: 0, y: 0, width: size, height: size))

let output = URL(fileURLWithPath: arguments[2]) as CFURL
let destination = CGImageDestinationCreateWithURL(output, "public.png" as CFString, 1, nil)!
CGImageDestinationAddImage(destination, context.makeImage()!, nil)
CGImageDestinationFinalize(destination)
