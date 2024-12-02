curl -fsSL https://ollama.com/install.sh | sh
sudo useradd -r -s /bin/false -U -m -d /usr/local/bin/ollama ollama
sudo usermod -a -G ollama $(whoami)