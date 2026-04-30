{ pkgs ? import <nixpkgs> {} }:

pkgs.mkShell {
  name = "intelligence-idx-env";

  buildInputs = with pkgs; [
    # Core build tools
    jdk21
    bazelisk
    git

    # Scripts and automation
    nodejs_22
    python312

    # System dependencies for IntelliJ
    libfreetype
    fontconfig
  ];

  shellHook = ''
    export JAVA_HOME=${pkgs.jdk21}/lib/openjdk
    export PATH=$JAVA_HOME/bin:$PATH
    echo "❄️ Welcome to the Intelligence IDX Cloud Environment"
    echo "JDK: $(java -version 2>&1 | head -n 1)"
    echo "Bazel: $(bazel --version)"
  '';
}
