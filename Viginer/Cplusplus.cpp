#include <iostream>
#include <cctype>
using namespace std;

string generateKey(string str, string key)
{
    int x = str.size();
    for (int i = 0; key.size() < x; i++)
    {
        if (i == key.size())
            i = 0;
        key.push_back(key[i]);
    }
    return key;
}

string cipherText(string str, string key)
{
    string cipher_text;
    for (int i = 0; i < str.size(); i++)
    {
        char x = (toupper(str[i]) + toupper(key[i]) - 2 * 'A') % 26 + 'A'; // Handle both uppercase and lowercase
        cipher_text.push_back(x);
    }
    return cipher_text;
}

string originalText(string cipher_text, string key)
{
    string orig_text;
    for (int i = 0; i < cipher_text.size(); i++)
    {
        char x = (toupper(cipher_text[i]) - toupper(key[i]) + 26) % 26 + 'A'; // Handle both uppercase and lowercase
        orig_text.push_back(x);
    }
    return orig_text;
}

int main()
{
    string str;
    string keyword;
    cout << "Enter a string: ";
    cin >> str;
    cout << "Enter a key: ";
    cin >> keyword;

    string key = generateKey(str, keyword);
    string cipher_text = cipherText(str, key);

    cout << "Ciphertext: " << cipher_text << "\n";
    cout << "Original/Decrypted Text: " << originalText(cipher_text, key) << endl;
    return 0;
}
