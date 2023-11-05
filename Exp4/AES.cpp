#include <iostream>
#include <string>
#include <vector>
#include <bitset>

// AES S-Box
const uint8_t sBox[256] = {
    0x63, 0x7C, 0x77, 0x7B, 0xF2, 0x6B, 0x6F, 0xC5, 0x30, 0x01, 0x67, 0x2B, 0xFE, 0xD7, 0xAB, 0x76,
    0xCA, 0x82, 0xC9, 0x7D, 0xFA, 0x59, 0x47, 0xF0, 0xAD, 0xD4, 0xA2, 0xAF, 0x9C, 0xA4, 0x72, 0xC0,
    0xB7, 0xFD, 0x93, 0x26, 0x36, 0x3F, 0xF7, 0xCC, 0x34, 0xA5, 0xE5, 0xF1, 0x71, 0xD8, 0x31, 0x15,
    // ... (The S-Box values continue)
};

// Key expansion function
void KeyExpansion(const uint8_t *key, std::vector<std::vector<uint8_t>> &roundKeys)
{
    // Key schedule
    roundKeys.push_back(std::vector<uint8_t>(key, key + 16));

    for (int round = 1; round <= 10; ++round)
    {
        std::vector<uint8_t> prevRoundKey = roundKeys[round - 1];

        // RotWord (circular byte rotation)
        uint8_t temp = prevRoundKey[0];
        prevRoundKey[0] = prevRoundKey[1];
        prevRoundKey[1] = prevRoundKey[2];
        prevRoundKey[2] = prevRoundKey[3];
        prevRoundKey[3] = temp;

        // SubWord (byte substitution using S-Box)
        for (int i = 0; i < 4; ++i)
        {
            prevRoundKey[i] = sBox[prevRoundKey[i]];
        }

        // XOR with the round constant (rcon)
        prevRoundKey[0] ^= (0x01 << round);

        // XOR with the first byte of the previous round's key
        for (int i = 0; i < 4; ++i)
        {
            prevRoundKey[i] ^= roundKeys[round - 1][i];
        }

        roundKeys.push_back(prevRoundKey);
    }
}

// AES encryption function
void AESEncrypt(const uint8_t *input, const std::vector<std::vector<uint8_t>> &roundKeys, uint8_t *output)
{
    // Initial round (AddRoundKey)
    for (int i = 0; i < 16; ++i)
    {
        output[i] = input[i] ^ roundKeys[0][i];
    }

    // Main rounds (SubBytes, ShiftRows, MixColumns, AddRoundKey)
    for (int round = 1; round <= 9; ++round)
    {
        // SubBytes (byte substitution using S-Box)
        for (int i = 0; i < 16; ++i)
        {
            output[i] = sBox[output[i]];
        }

        // ShiftRows
        // MixColumns (omitted in this simplified example)
        // AddRoundKey
        for (int i = 0; i < 16; ++i)
        {
            output[i] ^= roundKeys[round][i];
        }
    }

    // Final round (SubBytes, ShiftRows, AddRoundKey)
    for (int i = 0; i < 16; ++i)
    {
        output[i] = sBox[output[i]];
    }
    for (int i = 0; i < 16; ++i)
    {
        output[i] ^= roundKeys[10][i];
    }
}

int main()
{
    // 128-bit encryption key (16 bytes)
    uint8_t key[16] = {0x2b, 0x7e, 0x15, 0x16, 0x28, 0xae, 0xd2, 0xa6, 0xab, 0xf7, 0x97, 0x75, 0x46, 0x20, 0x63, 0xed};

    // Input plaintext (16 bytes)
    uint8_t plaintext[16] = "Hello, AES!1234";

    // Key expansion
    std::vector<std::vector<uint8_t>> roundKeys;
    KeyExpansion(key, roundKeys);

    // Encryption
    uint8_t ciphertext[16];
    AESEncrypt(plaintext, roundKeys, ciphertext);

    // Print the original text and ciphertext
    std::cout << "Original Text: " << plaintext << std::endl;
    std::cout << "Ciphertext: ";
    for (int i = 0; i < 16; ++i)
    {
        std::cout << std::hex << std::setw(2) << std::setfill('0') << (int)ciphertext[i];
    }
    std::cout << std::dec << std::endl;

    return 0;
}
