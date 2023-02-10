#  HTTplugins -> OPhabs
## Setting up the documentation:
#### Install Doxygen and Graphviz in Debian based systems:
```sh
sudo apt-get doxygen
sudo apt install graphviz
```

#### Install Doxygen and Graphviz in centOS based systems:
```sh
sudo dnf install doxygen
sudo dnf install graphviz
```
#### Install Doxygen and Graphviz in Fedora based systems:
```sh
sudo yum install doxygen
sudo yum install graphviz
```

#### Install Doxygenand and Graphviz in other systems (manual):
[Doxygen official manual](https://www.doxygen.nl/manual/install.html).
[Graphviz official manual](https://graphviz.org/download/).

## Compiling Documentation:
```sh
doxygen Doxyfile
```