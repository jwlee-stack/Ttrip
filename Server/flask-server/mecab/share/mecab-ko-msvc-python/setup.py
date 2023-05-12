#!/usr/bin/env python

from setuptools import setup,Extension

setup(name = "mecab-ko-msvc-python",
	version = "0.997",
	py_modules=["MeCab"],
	ext_modules = [
		Extension("_MeCab",
			["mecab_python.cpp",],
			include_dirs=["C:/mecab/include"],
			library_dirs=["C:/mecab/lib"],
			libraries=["libmecab"])
			])