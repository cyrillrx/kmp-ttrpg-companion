import AppKit

let arguments = CommandLine.arguments
guard arguments.count >= 4, let size = Int(arguments[3]) else {
    fatalError("usage: render_png.swift <input.svg> <output.png> <size> [--opaque]")
}

let inputPath = arguments[1]
let outputPath = arguments[2]
let opaque = arguments.contains("--opaque")

guard let svg = NSImage(contentsOf: URL(fileURLWithPath: inputPath)) else {
    fatalError("cannot load \(inputPath)")
}

let alphaInfo: CGImageAlphaInfo = opaque ? .noneSkipLast : .premultipliedLast
guard let context = CGContext(
    data: nil,
    width: size,
    height: size,
    bitsPerComponent: 8,
    bytesPerRow: 0,
    space: CGColorSpace(name: CGColorSpace.sRGB)!,
    bitmapInfo: alphaInfo.rawValue
) else {
    fatalError("cannot create a \(size) px bitmap context for \(outputPath)")
}

NSGraphicsContext.current = NSGraphicsContext(cgContext: context, flipped: false)
svg.draw(in: NSRect(x: 0, y: 0, width: size, height: size))

guard let image = context.makeImage() else {
    fatalError("cannot rasterize \(inputPath)")
}

guard let destination = CGImageDestinationCreateWithURL(
    URL(fileURLWithPath: outputPath) as CFURL,
    "public.png" as CFString,
    1,
    nil
) else {
    fatalError("cannot open \(outputPath) for writing")
}

CGImageDestinationAddImage(destination, image, nil)
guard CGImageDestinationFinalize(destination) else {
    fatalError("cannot write \(outputPath)")
}
